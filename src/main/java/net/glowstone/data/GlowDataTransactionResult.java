package net.glowstone.data;

import com.google.common.base.Optional;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataTransactionResult;

import java.util.ArrayList;
import java.util.Collection;

public class GlowDataTransactionResult implements DataTransactionResult {
    private final Type type;
    private final Collection<? extends DataManipulator<?>> rejected, replaced;

    public GlowDataTransactionResult(Type type, Collection<? extends DataManipulator<?>> rejected, Collection<? extends DataManipulator<?>> replaced) {
        this.type = type;
        this.rejected = rejected;
        this.replaced = replaced;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Optional<? extends Collection<? extends DataManipulator<?>>> getRejectedData() {
        return Optional.fromNullable(rejected);
    }

    @Override
    public Optional<? extends Collection<? extends DataManipulator<?>>> getReplacedData() {
        return Optional.fromNullable(replaced);
    }

    public static Builder builder(Type type) {
        return new Builder(type);
    }

    public static class Builder {
        private final Collection<DataManipulator<?>> rejected, replaced;
        private final Type type;

        private Builder(Type type) {
            this.type = type;
            rejected = new ArrayList<>();
            replaced = new ArrayList<>();
        }

        public Builder rejected(DataManipulator<?> dataManipulator) {
            rejected.add(dataManipulator);
            return this;
        }

        public Builder replaced(DataManipulator<?> dataManipulator) {
            replaced.add(dataManipulator);
            return this;
        }

        public GlowDataTransactionResult build() {
            return new GlowDataTransactionResult(Type.SUCCESS, rejected.isEmpty() ? null : rejected, replaced.isEmpty() ? null : replaced);
        }
    }
}
