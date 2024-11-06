package lol.aliaga.nuhc;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lol.aliaga.nuhc.board.UHCBoard;
import lol.aliaga.nuhc.commands.*;
import lol.aliaga.nuhc.game.GameConfig;
import lol.aliaga.nuhc.game.GameState;
import lol.aliaga.nuhc.listeners.PlayerJoinListener;
import lol.aliaga.nuhc.menus.*;
import lol.aliaga.nuhc.player.UHCPlayerManager;
import lol.aliaga.nuhc.scenarios.ScenarioManager;
import lol.aliaga.nuhc.team.UHCTeamManager;
import lol.aliaga.nuhc.utils.EmptyChunkGenerator;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class NUHC extends JavaPlugin {

    @Getter
    private static NUHC instance;

    @Getter
    private UHCPlayerManager uhcPlayerManager;
    @Getter
    private ScenarioManager scenarioManager;
    @Getter
    private UHCTeamManager uhcTeamManager;
    @Getter @Setter
    private GameState gameState;
    @Getter @Setter
    private GameConfig gameConfig;
    @Getter @Setter
    private int currentBorder;
    @Getter @Setter
    private long startTime;

    @Override
    public void onEnable() {
        instance = this;
        initializeManagers();
        registerListeners();
        registerCommands();
        setupScoreboard();
    }

    @Override
    public void onDisable() {
        // Aquí puedes manejar las tareas de limpieza si es necesario
    }

    private void initializeManagers() {
        gameState = GameState.SETUP;
        uhcPlayerManager = new UHCPlayerManager();
        gameConfig = new GameConfig();
        scenarioManager = new ScenarioManager();
        uhcTeamManager = new UHCTeamManager();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new MenuEventHandler(), this);
        getServer().getPluginManager().registerEvents(new ScenariosMenu(), this);
        getServer().getPluginManager().registerEvents(new AddScenarioMenu(), this);
        getServer().getPluginManager().registerEvents(new WorldMenu(), this);
        getServer().getPluginManager().registerEvents(new TopKillsMenu(), this);

    }

    private void registerCommands() {
        registerCommand("config", new ConfigCommand());
        registerCommand("uhc", new UHCCommand());
        registerCommand("team", new TeamCommand());
        registerCommand("uhccode", new UHCCodeCommand());
        registerCommand("teamchat", new TeamChatCommand());
        registerCommand("sc", new TeamCoordsCommand());
        registerCommand("loadchunks", new LoadChunksCommand());
        registerCommand("start", new StartCommand());
        registerCommand("helpop", new HelpopCommand());
        registerCommand("scenarios", new ScenariosCommand());
        registerCommand("giveall", new GiveAllCommand());
        registerCommand("heal", new HealCommand());
        registerCommand("clear", new ClearCommand());
        registerCommand("health", new HealthCommand());
        registerCommand("clearchat", new ClearChatCommand());
        registerCommand("world", new WorldCommand());
        registerCommand("kt", new KillTopCommand());
        registerCommand("tele", new TeleCommand());
        registerCommand("rules", new RulesCommand());


    }

    private void registerCommand(String name, CommandExecutor executor) {
        getCommand(name).setExecutor(executor);
    }

    private void setupScoreboard() {
        Assemble assemble = new Assemble(this, new UHCBoard());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.KOHI);
    }

}
