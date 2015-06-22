package subsistence.common.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import subsistence.common.asm.transformer.CoreTransformer;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class SubsistenceCorePlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"subsistence.common.asm.transformer.TransformerDelegate"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        //Get obfuscation status this way as it will return as not obfuscated for non-vanilla classes
        CoreTransformer.obfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
