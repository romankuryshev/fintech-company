package com.academy.fintech.scoring.core.processing.model;

public record ClientStatistic(
        int countProducts,
        int countOverdueProducts
) {
}
