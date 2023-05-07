package io.github.agus5534.bamboofightersv2.commands.manager.module;

import io.github.agus5534.bamboofightersv2.commands.manager.module.factory.SelectionTierFactory;
import io.github.agus5534.bamboofightersv2.commands.manager.module.factory.TimeFormatterFactory;
import io.github.agus5534.bamboofightersv2.team.PlayerSelection;
import io.github.agus5534.bamboofightersv2.utils.extra.TimeFormatter;
import me.fixeddev.commandflow.annotated.part.AbstractModule;

public class ExtraBukkitModule extends AbstractModule {

    @Override
    public void configure() {
        this.bindFactory(TimeFormatter.class, new TimeFormatterFactory());
        this.bindFactory(PlayerSelection.SelectionTier.class, new SelectionTierFactory());
    }
}
