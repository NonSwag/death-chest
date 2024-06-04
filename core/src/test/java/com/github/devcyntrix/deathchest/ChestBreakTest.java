package com.github.devcyntrix.deathchest;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.model.CraftDeathChestConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DisplayName("Chest breaking tests")
public class ChestBreakTest {

    private ServerMock server;

    private List<ItemStack> content;
    private PlayerMock player;
    private DeathChestModel model;

    @BeforeEach
    public void setUp() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("default-config.yml");
        if (stream == null)
            throw new IllegalStateException("Missing config");
        DeathChestConfig config;
        try (InputStreamReader reader = new InputStreamReader(stream)) {
            config = CraftDeathChestConfig.load(YamlConfiguration.loadConfiguration(reader));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.server = MockBukkit.getOrCreateMock();
        DeathChestCorePlugin plugin = MockBukkit.load(DeathChestCorePlugin.class, true, config);

        this.player = server.addPlayer();
        this.content = new ArrayList<>(List.of(new ItemStack(Material.OAK_LOG)));
        this.model = plugin.createDeathChest(player.getLocation(), content.toArray(ItemStack[]::new));
        server.getScheduler().performOneTick();

        Assertions.assertFalse(model.getLocation().getBlock().isEmpty());
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Block break by player")
    public void breakBlock() {
        Location location = player.getLocation();
        Block block = location.getBlock();

        System.out.println("Breaking block...");
        BlockBreakEvent blockBreakEvent = player.simulateBlockBreak(block);
        Assertions.assertNotNull(blockBreakEvent);
        Assertions.assertTrue(blockBreakEvent.isCancelled());
        Assertions.assertTrue(block.isEmpty());

        WorldMock world = player.getWorld();
        Collection<Item> items = world.getEntitiesByClass(Item.class);
        items.forEach(item -> content.remove(item.getItemStack()));
        Assertions.assertTrue(content.isEmpty());
    }

    @Test
    @DisplayName("Block break by entity explosion")
    public void entityExplodeBlock() {
        WorldMock mock = player.getWorld();
        Block block = model.getLocation().getBlock();

        System.out.println("Creating entity explosion...");
        Creeper spawn = mock.spawn(block.getLocation(), Creeper.class);
        EntityExplodeEvent event = new EntityExplodeEvent(spawn, model.getLocation(), new ArrayList<>(List.of(block)), 2.0F);
        server.getPluginManager().callEvent(event);
        Assertions.assertFalse(event.isCancelled());
        Assertions.assertTrue(block.isEmpty());

        WorldMock world = player.getWorld();
        Collection<Item> items = world.getEntitiesByClass(Item.class);
        items.forEach(item -> content.remove(item.getItemStack()));
        Assertions.assertTrue(content.isEmpty());
    }

    @Test
    @DisplayName("Block break by block explosion")
    public void blockExplodeBlock() {
        Block block = model.getLocation().getBlock();

        System.out.println("Creating block explosion...");
        BlockExplodeEvent event = new BlockExplodeEvent(block, new ArrayList<>(List.of(block)), 2.0F, null);
        server.getPluginManager().callEvent(event);
        Assertions.assertFalse(event.isCancelled());
        Assertions.assertTrue(block.isEmpty());

        WorldMock world = player.getWorld();
        Collection<Item> items = world.getEntitiesByClass(Item.class);
        items.forEach(item -> content.remove(item.getItemStack()));
        Assertions.assertTrue(content.isEmpty());
    }
}
