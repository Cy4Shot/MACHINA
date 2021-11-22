import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("test_mod")
public class TestMod {

	public TestMod() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
