package subsistence.common.recipe.wrapper.module.component;

import com.google.gson.JsonObject;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.recipe.wrapper.module.core.ModularObject;

/**
 * @author dmillerw
 */
public class ComponentFluid extends ModularObject {

    public FluidStack fluidStack;

    @Override
    public void acceptData(JsonObject jsonObject) {
        String fluid = getString(jsonObject, "fluid", "");
        int amount = getInt(jsonObject, "amount", FluidContainerRegistry.BUCKET_VOLUME);
        fluidStack = new FluidStack(FluidRegistry.getFluid(fluid), amount);
    }
}
