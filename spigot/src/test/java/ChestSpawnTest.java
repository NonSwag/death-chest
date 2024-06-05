import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.model.CraftDeathChestConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DisplayName("Chest spawn tests")
public class ChestSpawnTest {

    private ServerMock server;

    @BeforeEach
    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    public void setUp() {
        try (var stream = getClass().getClassLoader().getResourceAsStream("default-config.yml");
             var reader = new InputStreamReader(stream)) {
            this.server = MockBukkit.getOrCreateMock();
            server.setSpawnRadius(0);
            var config = CraftDeathChestConfig.load(YamlConfiguration.loadConfiguration(reader));
            var pdf = new PluginDescriptionFile(getClass().getClassLoader().getResourceAsStream("plugin.yml"));
            var main = (Class<? extends DeathChestCorePlugin>) Class.forName(pdf.getMainClass());
            MockBukkit.loadWith(main, pdf, config);
        } catch (IOException | InvalidDescriptionException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("No chest if inventory was empty")
    public void doesntSpawnEmptyChest() {
        PlayerMock player = server.addPlayer();
        @NotNull Location location = player.getLocation();
        player.setHealth(0.0);
        Assertions.assertTrue(location.getBlock().isEmpty());
    }

    @Test
    @DisplayName("Spawn filled chest")
    public void spawnFilledChest() {
        List<ItemStack> list = new ArrayList<>(List.of(new ItemStack(Material.OAK_LOG)));

        PlayerMock player = server.addPlayer();
        PlayerInventory inventory = player.getInventory();
        System.out.println("Adding item to the inventory...");
        inventory.addItem(list.toArray(ItemStack[]::new));

        @NotNull Location location = player.getLocation();
        System.out.println("Killing player...");
        player.setHealth(0.0);
        server.getScheduler().performOneTick();
        Block block = location.getBlock();
        Assertions.assertFalse(block.isEmpty());
        System.out.println("Breaking block...");

        BlockBreakEvent blockBreakEvent = player.simulateBlockBreak(block);
        Assertions.assertNotNull(blockBreakEvent);
        Assertions.assertTrue(blockBreakEvent.isCancelled());
        Assertions.assertTrue(block.isEmpty());

        WorldMock world = player.getWorld();
        Collection<Item> items = world.getEntitiesByClass(Item.class);
        items.forEach(item -> list.remove(item.getItemStack()));
        Assertions.assertTrue(list.isEmpty());
    }

}
