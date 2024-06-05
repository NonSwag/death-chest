import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.api.model.HologramOptions;
import com.github.devcyntrix.deathchest.model.CraftDeathChestConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DisplayName("Hologram tests")
public class ChestHologramTest {

    private DeathChestCorePlugin plugin;

    private DeathChestModel model;

    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    @BeforeEach
    public void setUp() throws ClassNotFoundException, InvalidDescriptionException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("default-config.yml");
        if (stream == null)
            throw new IllegalStateException("Missing config");
        DeathChestConfig config;
        try (InputStreamReader reader = new InputStreamReader(stream)) {
            config = CraftDeathChestConfig.load(YamlConfiguration.loadConfiguration(reader));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ServerMock server = MockBukkit.getOrCreateMock();
        var pdf = new PluginDescriptionFile(getClass().getClassLoader().getResourceAsStream("plugin.yml"));
        var main = (Class<? extends DeathChestCorePlugin>) Class.forName(pdf.getMainClass());
        this.plugin = MockBukkit.loadWith(main, pdf, config);

        PlayerMock player = server.addPlayer();
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
    @DisplayName("Hologram spawn")
    public void spawnHologram() {
        World world = model.getWorld();
        Assertions.assertNotNull(world);

        Collection<ArmorStand> armorStands = world.getEntitiesByClass(ArmorStand.class);
        HologramOptions hologramOptions = plugin.getDeathChestConfig().hologramOptions();
        boolean enabled = hologramOptions.enabled();

        Assertions.assertEquals(hologramOptions.enabled(), !armorStands.isEmpty());
        if (!enabled)
            return;
        Assertions.assertEquals(hologramOptions.lines().size(), armorStands.size());
    }

    @Test
    @DisplayName("Hologram remove")
    public void removeHologram() {
        World world = model.getWorld();
        Assertions.assertNotNull(world);

        plugin.getDeathChestController().destroyChest(model);

        Collection<ArmorStand> armorStands = world.getEntitiesByClass(ArmorStand.class);
        Assertions.assertTrue(armorStands.isEmpty());
    }

}
