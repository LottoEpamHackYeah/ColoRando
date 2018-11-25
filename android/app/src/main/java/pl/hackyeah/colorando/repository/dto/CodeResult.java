package pl.hackyeah.colorando.repository.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class CodeResult implements Serializable {

    private String solution;
    private String sharingId;
    private Boolean valid;

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSharingId() {
        return sharingId;
    }

    public void setSharingId(String sharingId) {
        this.sharingId = sharingId;
    }

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeResult that = (CodeResult) o;
        return valid == that.valid &&
                Objects.equals(solution, that.solution) &&
                Objects.equals(sharingId, that.sharingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solution, sharingId, valid);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("CodeResult{solution='%s', sharingId='%s', valid=%s}", solution, sharingId, valid);
    }
}
