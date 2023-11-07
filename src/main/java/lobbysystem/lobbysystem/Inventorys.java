package lobbysystem.lobbysystem;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Inventorys implements Listener {

    private Inventory BuildInventory;
    private static Inventory CompassInventory;

    private static ItemStack NavCompass;
    private static ItemStack SurvivalServer;
    private static ItemStack CreativeServer;
    private static ItemStack DisablePlugin;

    public Inventorys(LobbySystem plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        NavCompass = createItemStack(Material.COMPASS, "§6§fNavigation", "Rechtsklick um Navigator zu öffnen");
        SurvivalServer = createItemStack(Material.GRASS_BLOCK, "Survival Server", "Klicken um auf den Survival Server zu joinen");
        CreativeServer = createItemStack(Material.OAK_PLANKS, "Creativ Server", "Klicken um auf den Creative Server zu joinen");
        DisablePlugin = createItemStack(Material.BARRIER, "Disable LobbySystem",
            "Klicken um das LobbySystem zu deaktivieren um das Inventar normal nutzen zu können. Nutze '/EnableLobbySystem true' um das Plugin wieder zu aktivieren.");
        CompassInventory = Bukkit.createInventory(null, 3 * 9, "Navigator");
        CompassInventory.setItem(12, SurvivalServer);
        CompassInventory.setItem(14, CreativeServer);
        CompassInventory.setItem(26, DisablePlugin);
    }

    private static ItemStack createItemStack(Material material, String name, String lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        if (lore != null) {
            ArrayList<String> Lore = new ArrayList<>();
            Lore.add(lore);
            meta.setLore(Lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        if (LobbySystem.getEnableStatus()) {
            Player p = event.getPlayer();
            p.getInventory().clear();
            p.getInventory().setItem(0, NavCompass);
        }
    }

    public void onEnableReloadInventory() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            p.getInventory().setItem(0, NavCompass);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (LobbySystem.getEnableStatus()) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            Inventory inventory = event.getInventory();

            event.setCancelled(true);
            if (inventory.equals(CompassInventory)) {
                if (clicked.equals(SurvivalServer)) {
                    player.sendActionBar(Component.text("Survival Server"));
                    connectPlayerToServer("survival", player);
                } else if (clicked.equals(CreativeServer)) {
                    player.sendActionBar(Component.text("Creative Server"));
                    connectPlayerToServer("creative", player);
                } else if (clicked.equals(DisablePlugin)) {
                    player.sendActionBar(Component.text("Disabled LobbySystem"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.closeInventory();
                        p.getInventory().clear();
                    }
                    LobbySystem.setEnableStatus(false);
                }
            }
        }
    }

    private void connectPlayerToServer(String server, Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(LobbySystem.getLobbySystem(), "BungeeCord", out.toByteArray());
    }

    @EventHandler
    private void onRightClick(PlayerInteractEvent event) {
        if (LobbySystem.getEnableStatus()) {
            Player player = event.getPlayer();

            if (event.getAction().isRightClick()) {
                if (player.getItemInHand().equals(NavCompass)) {
                    player.openInventory(CompassInventory);
                }
            }
        }
    }

}
