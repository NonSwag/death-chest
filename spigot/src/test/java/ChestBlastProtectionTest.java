import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.model.CraftDeathChestConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DisplayName("Chest blast protection tests")
public class ChestBlastProtectionTest {

    private ServerMock server;

    private PlayerMock player;
    private DeathChestModel model;

    @BeforeEach
    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    public void setUp() throws InvalidDescriptionException, ClassNotFoundException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("chest-blast-protection.yml");
        if (stream == null)
            throw new IllegalStateException("Missing config");
        DeathChestConfig config;
        try (InputStreamReader reader = new InputStreamReader(stream)) {
            config = CraftDeathChestConfig.load(YamlConfiguration.loadConfiguration(reader));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.server = MockBukkit.getOrCreateMock();
        var pdf = new PluginDescriptionFile(getClass().getClassLoader().getResourceAsStream("plugin.yml"));
        var main = (Class<? extends DeathChestCorePlugin>) Class.forName(pdf.getMainClass());
        var plugin = MockBukkit.loadWith(main, pdf, config);

        this.player = server.addPlayer();
        List<ItemStack> content = new ArrayList<>(List.of(new ItemStack(Material.OAK_LOG)));
        this.model = plugin.createDeathChest(player.getLocation(), content.toArray(ItemStack[]::new));
        server.getScheduler().performOneTick();

        Assertions.assertFalse(model.getLocation().getBlock().isEmpty());
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
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

        Assertions.assertFalse(block.isEmpty());

        WorldMock world = player.getWorld();
        Collection<Item> items = world.getEntitiesByClass(Item.class);
        Assertions.assertTrue(items.isEmpty());
    }

    @Test
    @DisplayName("Block break by block explosion")
    public void blockExplodeBlock() {
        Block block = model.getLocation().getBlock();

        System.out.println("Creating block explosion...");
        BlockExplodeEvent event = new BlockExplodeEvent(block, block.getState(), new ArrayList<>(List.of(block)), 2.0F);
        server.getPluginManager().callEvent(event);
        Assertions.assertFalse(event.isCancelled());
        Assertions.assertFalse(block.isEmpty());

        WorldMock world = player.getWorld();
        Collection<Item> items = world.getEntitiesByClass(Item.class);
        Assertions.assertTrue(items.isEmpty());
    }
}
