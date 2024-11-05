package com.harleylizard.chunkage.target.predicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

public final class BlockPredicateTargetPredicate implements TargetPredicate {
    public static final MapCodec<BlockPredicateTargetPredicate> CODEC = RecordCodecBuilder.mapCodec(builder -> {
        return builder.group(BlockPredicate.CODEC.fieldOf("predicate").forGetter(BlockPredicateTargetPredicate::getPredicate)).apply(builder, BlockPredicateTargetPredicate::new);
    });

    private final BlockPredicate predicate;

    private BlockPredicateTargetPredicate(BlockPredicate predicate) {
        this.predicate = predicate;
    }

    public BlockPredicate getPredicate() {
        return predicate;
    }

    @Override
    public TargetPredicateType<? extends TargetPredicate> getTargetPredicateType() {
        return null;
    }
}
