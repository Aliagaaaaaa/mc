package lol.aliaga.nuhc;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lol.aliaga.nuhc.board.UHCBoard;
import lol.aliaga.nuhc.commands.*;
import lol.aliaga.nuhc.game.GameConfig;
import lol.aliaga.nuhc.game.GameState;
import lol.aliaga.nuhc.listeners.LobbyListener;
import lol.aliaga.nuhc.listeners.PlayerDeathListener;
import lol.aliaga.nuhc.menus.*;
import lol.aliaga.nuhc.player.UHCPlayerManager;
import lol.aliaga.nuhc.scenarios.ScenarioManager;
import lol.aliaga.nuhc.team.UHCTeamManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        gameState = GameState.SETUP;
        initializeManagers();
        registerListeners();
        registerCommands();
        setupScoreboard();

        World world = Bukkit.getWorld("lobby");
        world.setDifficulty(Difficulty.PEACEFUL);

    }

    @Override
    public void onDisable() {

    }

    private void initializeManagers() {
        uhcPlayerManager = new UHCPlayerManager();
        scenarioManager = new ScenarioManager();
        uhcTeamManager = new UHCTeamManager();
        gameConfig = new GameConfig();
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new LobbyListener(), this);
        pm.registerEvents(new PlayerDeathListener(), this);


        pm.registerEvents(new MenuEventHandler(), this);
        pm.registerEvents(new ScenariosMenu(), this);
        pm.registerEvents(new AddScenarioMenu(), this);
        pm.registerEvents(new WorldMenu(), this);
        pm.registerEvents(new TopKillsMenu(), this);
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
        PluginCommand command = getCommand(name);
        if (command != null) {
            command.setExecutor(executor);
        }
    }

    private void setupScoreboard() {
        Assemble assemble = new Assemble(this, new UHCBoard());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.KOHI);
    }
}