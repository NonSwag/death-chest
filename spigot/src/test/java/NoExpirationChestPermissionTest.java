import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.github.devcyntrix.deathchest.DeathChestCorePlugin;
import com.github.devcyntrix.deathchest.api.model.DeathChestConfig;
import com.github.devcyntrix.deathchest.api.model.DeathChestModel;
import com.github.devcyntrix.deathchest.api.model.NoExpirationPermission;
import com.github.devcyntrix.deathchest.model.CraftDeathChestConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@DisplayName("No expiration permission")
public class NoExpirationChestPermissionTest {

    private ServerMock server;
    private DeathChestCorePlugin plugin;

    private List<ItemStack> content;
    private PlayerMock player;

    @BeforeEach
    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    public void setUp() throws InvalidDescriptionException, ClassNotFoundException {
        try (var stream = getClass().getClassLoader().getResourceAsStream("no-expiration-chest-permission-config.yml");
             var reader = new InputStreamReader(stream)) {

            this.server = MockBukkit.getOrCreateMock();
            var config = CraftDeathChestConfig.load(YamlConfiguration.loadConfiguration(reader));
            var pdf = new PluginDescriptionFile(getClass().getClassLoader().getResourceAsStream("plugin.yml"));
            var main = (Class<? extends DeathChestCorePlugin>) Class.forName(pdf.getMainClass());
            this.plugin = MockBukkit.loadWith(main, pdf, config);

            this.player = server.addPlayer();
            this.content = new ArrayList<>(List.of(new ItemStack(Material.OAK_LOG)));
        } catch (IOException | InvalidDescriptionException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    private DeathChestModel createChest(Player player) {
        DeathChestConfig config = plugin.getDeathChestConfig();
        Duration expiration = config.chestOptions().expiration();
        if (expiration == null)
            expiration = Duration.ofSeconds(-1);

        plugin.debug(1, "Checking no expiration permission...");
        NoExpirationPermission permission = config.chestOptions().noExpirationPermission();
        boolean expires = !permission.enabled() || !player.hasPermission(permission.permission());
        long createdAt = System.currentTimeMillis();
        long expireAt = !expiration.isNegative() && !expiration.isZero() && expires ? createdAt + expiration.toMillis() : -1;

        var model = plugin.createDeathChest(player.getLocation(), createdAt, expireAt, player, content.toArray(ItemStack[]::new));
        server.getScheduler().performOneTick();

        Assertions.assertFalse(model.getLocation().getBlock().isEmpty());
        return model;
    }

    @Test
    @DisplayName("Has permission and doesn't expires")
    public void noExpirationPermissionSuccess() {
        this.player.addAttachment(plugin, plugin.getDeathChestConfig().chestOptions().noExpirationPermission().permission(), true);

        var model = createChest(this.player);

        long remainingSeconds = (model.getExpireAt() - model.getCreatedAt()) / 1000;
        System.out.printf("Skipping %d seconds%n", remainingSeconds);
        server.getScheduler().performTicks(remainingSeconds * 20 + 10);
        Assertions.assertFalse(model.getLocation().getBlock().isEmpty(), "Chest removed after %d seconds".formatted(remainingSeconds));
    }

    @Test
    @DisplayName("Has no permission and expires")
    public void noExpirationPermissionFail() {
        var model = createChest(player);

        long remainingSeconds = (model.getExpireAt() - model.getCreatedAt()) / 1000;
        System.out.printf("Skipping %d seconds%n", remainingSeconds);
        server.getScheduler().performTicks(remainingSeconds * 20 + 10);
        Assertions.assertTrue(model.getLocation().getBlock().isEmpty(), "Chest not removed after %d seconds".formatted(remainingSeconds));
    }

}
