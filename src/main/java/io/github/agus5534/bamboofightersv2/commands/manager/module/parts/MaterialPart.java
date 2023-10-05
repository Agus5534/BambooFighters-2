package io.github.agus5534.bamboofightersv2.commands.manager.module.parts;

import io.github.agus5534.agusutils.annotations.arguments.BuildFactory;
import io.github.agus5534.agusutils.annotations.arguments.Factory;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Factory(c = Material.class)
@BuildFactory
public class MaterialPart implements ArgumentPart {

    private final String name;

    public MaterialPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        List<String> strings = new ArrayList<>();

        Arrays.stream(Material.values()).forEach(m -> strings.add(m.name()));

        return strings;
    }

    @Override
    public List<?> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.parseMaterial(argumentStack));
    }
    
    public Material parseMaterial(ArgumentStack stack) {
        try {
            return Material.valueOf(stack.next());
        } catch (Exception e) {
            throw new ArgumentParseException("Invalid Material");
        }
    }
}
