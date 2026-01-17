Nama : Kasih Ananda Nardi
NIM   : 2311081021
Kelas  : TRPL 3D
Mata Kuliah : Arsitektur Berbasis Layanan

FASE 1: PERSIAPAN SOURCE CODE JAVA
Lakukan konfigurasi ini di dalam IntelliJ IDEA / VS Code pada setiap project Microservice 
1.1.	Konfigurasi pom.xml 
Tambahkan dependency ini agar service bisa dimonitor oleh Prometheus. Buka pom.xml, masukkan di dalam tag <dependencies>:

<!-- Actuator: Untuk mengekspos metrics -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Micrometer: Agar Actuator bisa dibaca Prometheus -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>

1.2.	Konfigurasi application.properties
Buka file src/main/resources/application.properties.
Berikut adalah konfigurasi yang sudah disesuaikan dengan Kubernetes berdasarkan input Anda.
Contoh untuk anggotaservice:
spring.application.name=anggotaservice
server.port=9001

# --- DATABASE (H2) ---
spring.datasource.url=jdbc:h2:mem:anggotadb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
# Agar console H2 bisa diakses
spring.h2.console.enabled=true 


# --- EUREKA (SERVICE DISCOVERY) ---
# PENTING: Di Kubernetes, kita arahkan ke nama service 'eureka-server'
eureka.client.service-url.defaultZone=http://eureka-server:8761/eureka/

# Agar IP Pod yang didaftarkan, bukan hostname acak container
eureka.instance.prefer-ip-address=true
# (Opsional) Status Page untuk link di Dashboard Eureka
eureka.instance.status-page-url=http://localhost:9001/api/anggota/

# --- MONITORING (ACTUATOR & PROMETHEUS) ---
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
management.metrics.tags.application=${spring.application.name}

Lakukan hal serupa untuk service lain (Buku, Peminjaman, dll). Sesuaikan server.port dan spring.application.name-nya.
1.3.	Membuat Dockerfile
Buat file baru bernama Dockerfile (tanpa ekstensi .txt) di dalam folder utama setiap service (sejajar dengan pom.xml).
Isi Dockerfile:
# Gunakan base image Java 17 yang ringan
FROM openjdk:17-jdk-alpine

# Tentukan folder kerja di dalam container
WORKDIR /app

# Copy file jar hasil build ke dalam container
COPY target/*.jar app.jar

# Jalankan aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]

FASE 2: MEMPERSIAPKAN INFRASTRUKTUR KUBERNETES
Buka terminal PowerShell, masuk ke folder kubernetes.
2.1. Membuat Namespace & Secret
Namespace untuk memisahkan aplikasi kita, dan Secret untuk password.
File: namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: perpustakaan-ns
Jalankan:
kubectl apply -f namespace.yaml

2.2. Deploy Eureka Server (Jantung Aplikasi)
Service lain tidak akan jalan kalau ini belum hidup.
File: eureka-deployment.yaml
	apiVersion: apps/v1
kind: Deployment
metadata:
  name: eureka-deployment
  namespace: perpustakaan-ns
spec:
  replicas: 1
  selector:
    matchLabels:
      app: eureka-server
  template:
    metadata:
      labels:
        app: eureka-server
    spec:
      containers:
        - name: eureka-container
          image: eureka-perpustakaan-service:1.0  # Pastikan sudah dibuild
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 8761
---
apiVersion: v1
kind: Service
metadata:
  name: eureka-server   # Nama ini harus sama dengan di application.properties
  namespace: perpustakaan-ns
spec:
  selector:
    app: eureka-server
  ports:
    - protocol: TCP
      port: 8761
      targetPort: 8761
Jalankan:
# Build image dulu 
docker build -t eureka-perpustakaan-service:1.0 ../eurekaperpustakaan
# Deploy
kubectl apply -f eureka-deployment.yaml
FASE 3: DEPLOY APLIKASI MICROSERVICES
Kita ambil contoh anggotaservice. Buat file YAML-nya.
File: anggota-service-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: anggota-deployment
  namespace: perpustakaan-ns
spec:
  replicas: 1
  selector:
    matchLabels:
      app: anggota-service
  template:
    metadata:
      labels:
        app: anggota-service
    spec:
      containers:
        - name: anggota-container
          image: anggota-service:1.0      # Image hasil build Jenkins/Manual
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 9001
          env:
            # Overwrite config properties 
            - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
              value: "http://eureka-server:8761/eureka/"
---
apiVersion: v1
kind: Service
metadata:
  name: anggota-service
  namespace: perpustakaan-ns
  labels:
    app: anggota-service
  annotations:
    # Ini agar Prometheus otomatis mengambil data
    prometheus.io/scrape: "true"
    prometheus.io/path: "/actuator/prometheus"
    prometheus.io/port: "9001"
spec:
  type: LoadBalancer  # Agar bisa diakses di localhost:9001
  selector:
    app: anggota-service
  ports:
    - protocol: TCP
      port: 9001
      targetPort: 9001
Jalankan:
# Build manual dulu (sebelum ada Jenkins)
docker build -t anggota-service:1.0 ../anggotaservice
# Deploy
kubectl apply -f anggota-service-deployment.yaml
(Ulangi langkah ini untuk bukuservice, peminjamanservice, dll).
FASE 4: SETUP MONITORING (PROMETHEUS & GRAFANA)
Pastikan file-file ini ada di folder kubernetes/monitoring/.
4.1. Setup Dasar Monitoring
Jalankan perintah ini:
kubectl apply -f monitoring/monitoring-namespace.yaml
kubectl apply -f monitoring/prometheus-configmap.yaml
kubectl apply -f monitoring/prometheus-rbac.yaml

4.2. Deploy Prometheus
File: monitoring/prometheus-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: prometheus-deployment
  namespace: monitoring-ns
spec:
  replicas: 1
  selector:
    matchLabels:
      app: prometheus
  template:
    metadata:
      labels:
        app: prometheus
    spec:
      containers:
        - name: prometheus
          image: prom/prometheus:latest
          args:
            - "--config.file=/etc/prometheus/prometheus.yml"
          ports:
            - containerPort: 9090
          volumeMounts:
            - name: config-volume
              mountPath: /etc/prometheus/
      volumes:
        - name: config-volume
          configMap:
            name: prometheus-config
---
apiVersion: v1
kind: Service
metadata:
  name: prometheus-service
  namespace: monitoring-ns
spec:
  type: NodePort
  selector:
    app: prometheus
  ports:
    - port: 9090
      targetPort: 9090
Jalankan: kubectl apply -f monitoring/prometheus-deployment.yaml
4.3. Deploy Grafana
File: monitoring/grafana-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: grafana-deployment
  namespace: monitoring-ns
spec:
  replicas: 1
  selector:
    matchLabels:
      app: grafana
  template:
    metadata:
      labels:
        app: grafana
    spec:
      containers:
        - name: grafana
          image: grafana/grafana:latest
          ports:
            - containerPort: 3000
---
apiVersion: v1
kind: Service
metadata:
  name: grafana-service
  namespace: monitoring-ns
spec:
  type: LoadBalancer   # Agar bisa akses localhost:3000
  selector:
    app: grafana
  ports:
    - port: 3000
      targetPort: 3000
Jalankan: kubectl apply -f monitoring/grafana-deployment.yaml

FASE 5: SETUP LOGGING (ELK STACK)
Pastikan file-file ini ada di folder kubernetes/logging/.
5.1. Namespace Logging
	kubectl create namespace logging-ns
5.2. Elasticsearch (Database Log)
File: logging/elasticsearch.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: elasticsearch
  namespace: logging-ns
spec:
  replicas: 1
  selector:
    matchLabels:
      app: elasticsearch
  template:
    metadata:
      labels:
        app: elasticsearch
    spec:
      containers:
        - name: elasticsearch
          # Pakai image resmi versi stabil
          image: docker.elastic.co/elasticsearch/elasticsearch:7.17.10
          imagePullPolicy: IfNotPresent
          env:
            - name: discovery.type
              value: single-node
            - name: ES_JAVA_OPTS
              value: "-Xms512m -Xmx512m"
          ports:
            - containerPort: 9200
---
apiVersion: v1
kind: Service
metadata:
  name: elasticsearch
  namespace: logging-ns
spec:
  selector:
    app: elasticsearch
  ports:
    - port: 9200
      targetPort: 9200
Jalankan: kubectl apply -f logging/elasticsearch.yaml
(WAJIB TUNGGU sampai status Running sebelum lanjut).
5.3. Kibana (Dashboard Log)
File: logging/kibana.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kibana
  namespace: logging-ns
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kibana
  template:
    metadata:
      labels:
        app: kibana
    spec:
      containers:
        - name: kibana
          image: docker.elastic.co/kibana/kibana:7.17.10
          imagePullPolicy: IfNotPresent
          env:
            - name: ELASTICSEARCH_HOSTS
              value: "http://elasticsearch:9200"
          ports:
            - containerPort: 5601
---
apiVersion: v1
kind: Service
metadata:
  name: kibana
  namespace: logging-ns
spec:
  type: LoadBalancer # Agar akses localhost:5601
  selector:
    app: kibana
  ports:
    - port: 5601
      targetPort: 5601
Jalankan: kubectl apply -f logging/kibana.yaml

FASE 6: SETUP CI/CD (JENKINS DI WINDOWS HOST)
Ini langkah agar otomatisasi berjalan di laptop Windows Anda.
6.1. Persiapan Config
1.	Buat folder di C drive: C:\KubeConfig.
2.	Copy file config dari C:\Users\reyke\.kube\config ke dalam folder C:\KubeConfig.
3.	Pastikan nama filenya config (tanpa ekstensi).
6.2. Script Pipeline (Jenkinsfile)
Gunakan script ini di Jenkins Job Anda (tipe Pipeline).
Environment Variables yang harus disesuaikan per Job:
•	SERVICE_NAME: Nama folder di GitHub/Laptop (anggotaservice, bukuservice).
•	IMAGE_NAME: Nama image (anggota-service:1.0).
•	DEPLOYMENT_NAME: Nama deployment di YAML (anggota-deployment).
Script Lengkap:
pipeline {
    agent any
    
    environment {
        // --- SESUAIKAN BAGIAN INI UNTUK SETIAP SERVICE ---
        SERVICE_NAME = 'anggotaservice' 
        IMAGE_NAME = 'anggota-service:1.0'
        DEPLOYMENT_NAME = 'anggota-deployment' 
        NAMESPACE = 'perpustakaan-ns'
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo ' Mengambil Source Code dari GitHub...'
                git branch: 'main', url: 'https://github.com/ReykelRaflen/PerpustakaanService.git'
            }
        }

        stage('Build Java (Maven)') {
            steps {
                script {
                    dir("${SERVICE_NAME}") {
                        echo " Building JAR..."
                        // Menggunakan Maven Wrapper versi Windows
                        bat 'mvnw.cmd clean package -DskipTests'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dir("${SERVICE_NAME}") {
                        echo " Building Docker Image..."
                        // Menggunakan Docker Desktop di Windows
                        bat "docker build -t ${IMAGE_NAME} ."
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo " Deploying to Kubernetes..."
                    // Memaksa kubectl menggunakan config yang sudah kita siapkan
                    bat "kubectl --kubeconfig=C:\\KubeConfig\\config rollout restart deployment/${DEPLOYMENT_NAME} -n ${NAMESPACE}"
                }
            }
        }
    }
}

FASE 7: CARA AKSES (FINAL CHECK)
Setelah semua langkah di atas dijalankan:
1.	Aplikasi Anggota:
o	http://localhost:9001/actuator/health (Cek status)
o	http://localhost:9001/api/anggota (Cek data)
2.	Monitoring (Grafana):
o	Buka http://localhost:3000
o	Login admin/admin.
o	Add Data Source Prometheus: http://prometheus-service.monitoring-ns:9090
o	Import Dashboard ID 4701.
3.	Logging (Kibana):
o	Buka http://localhost:5601
o	Menu Management -> Index Patterns -> Create logstash-* (atau * jika logstash belum kirim data).
o	Menu Discover -> Lihat Log.
