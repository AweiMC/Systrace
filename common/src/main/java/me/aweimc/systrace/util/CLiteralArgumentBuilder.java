package me.aweimc.systrace.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CLiteralArgumentBuilder<S> extends LiteralArgumentBuilder<S> {
    private final String literal;

    public CLiteralArgumentBuilder(final String literal) {
        super(literal);
        this.literal = literal;
    }

    @Contract("_ -> new")
    public static <S> @NotNull CLiteralArgumentBuilder<S> of(String literal) {
        return new CLiteralArgumentBuilder<>(literal);
    }

    @Override
    protected LiteralArgumentBuilder<S> getThis() {
        return super.getThis();
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public LiteralCommandNode<S> build() {
        final LiteralCommandNode<S> result = new LiteralCommandNode<>(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
