package com.harleylizard.chunkage.target.predicate;

public sealed interface TargetPredicate permits BlockPredicateTargetPredicate {

    TargetPredicateType<? extends TargetPredicate> getTargetPredicateType();
}
