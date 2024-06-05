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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@DisplayName("World filter test")
public class WorldFilterTest {

    private ServerMock server;

    private PlayerMock player;

    @BeforeEach
    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    public void setUp() throws InvalidDescriptionException, ClassNotFoundException {

        try (var stream = getClass().getClassLoader().getResourceAsStream("default-config.yml");
             var reader = new InputStreamReader(stream)) {
            this.server = MockBukkit.getOrCreateMock();
            server.setSpawnRadius(0);
            var config = CraftDeathChestConfig.load(YamlConfiguration.loadConfiguration(reader));
            var pdf = new PluginDescriptionFile(getClass().getClassLoader().getResourceAsStream("plugin.yml"));
            var main = (Class<? extends DeathChestCorePlugin>) Class.forName(pdf.getMainClass());
            MockBukkit.loadWith(main, pdf, config);

            var content = new ArrayList<>(List.of(new ItemStack(Material.OAK_LOG)));
            this.player = server.addPlayer();
            player.getInventory().addItem(content.toArray(ItemStack[]::new));
        } catch (IOException | InvalidDescriptionException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Try to spawn in filtered world")
    public void dieInFilteredWorld() {
        WorldMock mock = server.addSimpleWorld("disabled_world");
        player.teleport(player.getLocation().toLocation(mock));
        @NotNull Location location = player.getLocation();
        player.setHealth(0.0);
        server.getScheduler().performOneTick();

        Block block = location.getBlock();
        Assertions.assertTrue(block.isEmpty());

        // Cannot check drops because of missing drop functionality of mock bukkit
        /*
        WorldMock world = (WorldMock) location.getWorld();
        Collection<Item> items = world.getEntitiesByClass(Item.class);
        System.out.println(items);
        items.forEach(item -> content.remove(item.getItemStack()));
        Assertions.assertTrue(content.isEmpty());
         */
    }

    @Test
    @DisplayName("Try to spawn in normal world")
    public void dieInNormalWorld() {
        System.out.println("Killing player...");
        player.setHealth(0.0);
        System.out.println("Perform tick");
        server.getScheduler().performOneTick();

        @NotNull Location location = player.getLocation();
        Block block = location.getBlock();
        System.out.println("Checking block...");
        Assertions.assertFalse(block.isEmpty());
        Assertions.assertTrue(location.getWorld().getEntitiesByClass(Item.class).isEmpty());

        // Cannot check drops because of missing drop functionality of mock bukkit
//        WorldMock world = (WorldMock) location.getWorld();
//        Collection<Item> items = world.getEntitiesByClass(Item.class);
//        System.out.println(items);
//        items.forEach(item -> content.remove(item.getItemStack()));
//        Assertions.assertTrue(content.isEmpty());
    }

}
