package com.kruten.jarsofttesttask.validator;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    public ErrorResponse() {
    }

    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }
}
