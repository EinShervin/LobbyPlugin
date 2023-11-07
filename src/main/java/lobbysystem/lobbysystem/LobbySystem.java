package lobbysystem.lobbysystem;

import org.bukkit.plugin.java.JavaPlugin;

public final class LobbySystem extends JavaPlugin {

    private static LobbySystem lobbySystem;
    private static Inventorys inventorys;
    private static boolean EnableStatus;
    @Override
    public void onEnable() {
        //getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord:main");
        EnableStatus = true;
        lobbySystem = this;
        inventorys = new Inventorys(this);
        getCommand("EnableLobbySystem").setExecutor(new Disable());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LobbySystem getLobbySystem(){
        return lobbySystem;
    }

    public static void setEnableStatus(boolean value){
        EnableStatus = value;
        if (value){
           inventorys.onEnableReloadInventory();
        }
    }

    public static boolean getEnableStatus(){ return EnableStatus; }
}
