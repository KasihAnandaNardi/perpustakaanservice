package com.pengembalianservice.pengembalianservice.vo;

import com.pengembalianservice.pengembalianservice.model.Pengembalian;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PengembalianResponseTemplateVO {
    private Pengembalian pengembalian;
    private PeminjamanView peminjamanDetail;
}
