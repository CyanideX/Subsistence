package oldStuff.common.lib.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import oldStuff.client.render.tile.RenderTileTable;

import java.util.Random;

public enum EnumParticle {

    HUGE_EXPLOSION("hugeexplosion"),
    LARGE_EXPLOSION("largeexplode"),
    SMALL_EXPLOSION("explode"),
    FIREWORK_SPARK("fireworksSpawk"),
    BUBBLE("bubble"),
    SUSPEND("suspended"),
    DEPTH_SUSPEND("depthsuspend"),
    TOWN_AURA("townaura"),
    CRITICAL("crit"),
    CRITICAL_MAGIC("magicCrit"),
    SMOKE("smoke"),
    MOB_SPELL("mobSpell"),
    MOB_SPELL_AMBIENT("mobSpellAmbient"),
    SPELL("spell"),
    SPELL_INSTANT("instantSpell"),
    WITCH_MAGIC("witchMagic"),
    NOTE("note"),
    ENDER("portal"),
    ENCHANT_GLYPH("enchantmenttable"),
    FLAME("flame"),
    LAVA("lava"),
    FOOTSTEP("footstep"),
    SPLASH("splash"),
    LARGE_SMOKE("largesmoke"),
    CLOUD("cloud"),
    REDSTONE("reddust"),
    SNOWBALL_BREAK("snowballpoof"),
    DRIP_WATER("dripWater"),
    DRIP_LAVA("dripLava"),
    SLIME("slime"),
    HEART("heart"),
    VILLAGER_ANGRY("angryVillager"),
    VILLAGER_HAPPY("happyVillager");

    private String particle;

    EnumParticle(String particle) {
        this.particle = particle;
    }

    @SideOnly(Side.CLIENT)
    public void display(World world, double x, double y, double z, double vx, double vy, double vz) {
        world.spawnParticle(this.particle, x, y, z, vx, vy, vz);
    }

    // Special particles
    @SideOnly(Side.CLIENT)
    public static void ITEM_BREAK(ItemStack stack, World world, double x, double y, double z) {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            Vec3 vec3 = Vec3.createVectorHelper(((double) rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3.rotateAroundY(-rand.nextInt(360) * (float) Math.PI / 180.0F);

            double motionX = vec3.xCoord + 0.5F * (rand.nextFloat() * (rand.nextBoolean() ? -1.0F : 1.0F));
            double motionZ = vec3.xCoord + 0.5F * (rand.nextFloat() * (rand.nextBoolean() ? -1.0F : 1.0F));

            net.minecraft.client.particle.EntityBreakingFX entityBreakingFX = new net.minecraft.client.particle.EntityBreakingFX(world, x, y + RenderTileTable.STONE_RENDER_MAX, z, motionX, 0.25, motionZ, stack.getItem(), stack.getItemDamage());
            Minecraft.getMinecraft().effectRenderer.addEffect(entityBreakingFX);
        }
    }

}
