package com.kirin.outlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class SoftDeletes {

    /**
     * Дата и время удаления строки в таблице, может быть null
     */
    // @JsonIgnore
    @Column(insertable = false)
    private Timestamp deletedAt;

    public void setDelete() {
        if (deletedAt == null) deletedAt = Timestamp.valueOf(LocalDateTime.now());
    }

    public void cancelDelete() {
        deletedAt = null;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deletedAt != null;
    }
}
