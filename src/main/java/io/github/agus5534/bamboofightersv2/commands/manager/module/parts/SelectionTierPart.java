package io.github.agus5534.bamboofightersv2.commands.manager.module.parts;

import io.github.agus5534.bamboofightersv2.team.PlayerSelection;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectionTierPart implements ArgumentPart {
    private final String name;

    public SelectionTierPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        Arrays.stream(PlayerSelection.SelectionTier.values()).forEach(sT -> suggestions.add(sT.name().replaceAll("_", "")));

        return suggestions;
    }

    @Override
    public List<?> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.parseTier(argumentStack));
    }

    private PlayerSelection.SelectionTier parseTier(ArgumentStack s) {
        try {
            return PlayerSelection.SelectionTier.valueOf(s.next());
        } catch (Exception e) {
            throw new ArgumentParseException("Invalid SelectionTierPart");
        }
    }
}
