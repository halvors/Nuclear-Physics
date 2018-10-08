package org.halvors.nuclearphysics.common.block.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.client.render.block.BlockRenderingHandler;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockInventory;
import org.halvors.nuclearphysics.common.tile.TileMachine;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.nuclearphysics.common.type.EnumParticleType;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockMachine extends BlockInventory {
    private static final Map<EnumMachine, IIcon> iconMap = new HashMap<>(); // Note: Having this client side only causes crash for unknown reasons.

    public BlockMachine() {
        super("machine", Material.iron);

        setHardness(3.5F);
        setResistance(16);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        for (final EnumMachine type : EnumMachine.values()) {
            if (type.hasIcon()) {
                iconMap.put(type, iconRegister.registerIcon(Reference.PREFIX + type.getName()));
            }
        }

        super.registerIcons(iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        final EnumMachine type = EnumMachine.values()[metadata];

        if (type.hasIcon()) {
            return iconMap.get(type);
        }

        return super.getIcon(side, metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.getInstance().getRenderId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        for (final EnumMachine type : EnumMachine.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);
        final EnumMachine type = EnumMachine.values()[pos.getBlockMetadata(world)];

        if (tile instanceof TileMachine) {
            final TileMachine tileMachine = (TileMachine) tile;

            if (type.hasParticle() && tileMachine.getOperatingTicks() > 0) {
                final float xRandom = (float) pos.getX() + 0.5F;
                final float yRandom = (float) pos.getY() + 0.2F + random.nextFloat() * 6.0F / 16;
                final float zRandom = (float) pos.getZ() + 0.5F;
                final float iRandom = 0.52F;
                final float jRandom = random.nextFloat() * 0.6F - 0.3F;
                final double xSpeed = 0;
                final double ySpeed = type.getParticleSpeed();
                final double zSpeed = 0;

                switch (tileMachine.getFacing()) {
                    case NORTH:
                        if (!type.hasCustomParticle()) {
                            world.spawnParticle(type.getParticleType(), (xRandom + jRandom), yRandom, (zRandom - iRandom), xSpeed, ySpeed, zSpeed);
                        } else {
                            RenderUtility.renderParticle(type.getCustomParticleType(), world, (xRandom + jRandom), yRandom, (zRandom - iRandom), xSpeed, ySpeed, zSpeed);
                        }
                        break;

                    case SOUTH:
                        if (!type.hasCustomParticle()) {
                            world.spawnParticle(type.getParticleType(), (xRandom + jRandom), yRandom, (zRandom + iRandom), xSpeed, ySpeed, zSpeed);
                        } else {
                            RenderUtility.renderParticle(type.getCustomParticleType(), world, (xRandom + jRandom), yRandom, (zRandom + iRandom), xSpeed, ySpeed, zSpeed);
                        }
                        break;

                    case WEST:
                        if (!type.hasCustomParticle()) {
                            world.spawnParticle(type.getParticleType(), (xRandom - iRandom), yRandom, (zRandom + jRandom), xSpeed, ySpeed, zSpeed);
                        } else {
                            RenderUtility.renderParticle(type.getCustomParticleType(), world, (xRandom - iRandom), yRandom, (zRandom + jRandom), xSpeed, ySpeed, zSpeed);
                        }
                        break;

                    case EAST:
                        if (!type.hasCustomParticle()) {
                            world.spawnParticle(type.getParticleType(), (xRandom + iRandom), yRandom, (zRandom + jRandom), xSpeed, ySpeed, zSpeed);
                        } else {
                            RenderUtility.renderParticle(type.getCustomParticleType(), world, (xRandom + iRandom), yRandom, (zRandom + jRandom), xSpeed, ySpeed, zSpeed);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onBlockAdded ( final World world, final int x, final int y, final int z){
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);

        if (tile instanceof TileMachine) {
            ((TileMachine) tile).updatePower();
        }
    }

    @Override
    public void onBlockPlacedBy ( final World world, final int x, final int y, final int z,
    final EntityLivingBase entity, final ItemStack itemStack){
        world.setBlockMetadataWithNotify(x, y, z, itemStack.getMetadata(), 2);

        super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
    }

    @Override
    public int damageDropped ( final int metadata){
        return metadata;
    }

    @Override
    public boolean onBlockActivated ( final World world, final int x, final int y, final int z,
    final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ){
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);
        final ItemStack itemStack = player.getHeldItem();

        if (tile instanceof TilePlasmaHeater) {
            return FluidUtility.playerActivatedFluidItem(world, pos, player, itemStack, ForgeDirection.getOrientation(side));
        } else if (!player.isSneaking()) {
            PlayerUtility.openGui(player, world, pos);

            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void onNeighborBlockChange ( final World world, final int x, final int y, final int z,
    final Block neighbor){
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);

        if (tile instanceof TileMachine) {
            ((TileMachine) tile).updatePower();
        }
    }

    @Override
    public TileEntity createTileEntity ( final World world, final int metadata){
        final EnumMachine type = EnumMachine.values()[metadata];

        return type.getTileAsInstance();
    }

    public enum EnumMachine {
        CHEMICAL_EXTRACTOR(TileChemicalExtractor.class, EnumParticleType.RADIOACTIVE),
        GAS_CENTRIFUGE(TileGasCentrifuge.class),
        NUCLEAR_BOILER(TileNuclearBoiler.class, "cloud", 0.025),
        PARTICLE_ACCELERATOR(TileParticleAccelerator.class, true),
        PLASMA_HEATER(TilePlasmaHeater.class),
        QUANTUM_ASSEMBLER(TileQuantumAssembler.class);

        private final Class<? extends TileEntity> tileClass;
        private boolean icon;

        private boolean particle;
        private boolean customParticle;
        private String particleType;
        private EnumParticleType customParticleType;
        private double particleSpeed;

        EnumMachine(final Class<? extends TileEntity> tileClass, final boolean renderType) {
            this.tileClass = tileClass;
            this.icon = icon;
        }

        EnumMachine(final Class<? extends TileEntity> tileClass) {
            this(tileClass, false);
        }

        EnumMachine(final Class<? extends TileEntity> tileClass, final String particleType, final double particleSpeed) {
            this(tileClass);

            this.particle = true;
            this.particleType = particleType;
            this.particleSpeed = particleSpeed;
        }

        EnumMachine(final Class<? extends TileEntity> tileClass, final String particleType) {
            this(tileClass, particleType, 0);
        }

        EnumMachine(final Class<? extends TileEntity> tileClass, final EnumParticleType customParticleType, final double particleSpeed) {
            this(tileClass);

            this.particle = true;
            this.customParticle = true;
            this.customParticleType = customParticleType;
            this.particleSpeed = particleSpeed;
        }

        EnumMachine(final Class<? extends TileEntity> tileClass, final EnumParticleType customParticleType) {
            this(tileClass, customParticleType, 0);
        }

        public String getName() {
            return name().toLowerCase();
        }

        public Class<? extends TileEntity> getTileClass() {
            return tileClass;
        }

        public TileEntity getTileAsInstance() {
            try {
                return tileClass.newInstance();
            } catch (Exception e) {
                NuclearPhysics.getLogger().error("Unable to indirectly create tile entity.");
                e.printStackTrace();
            }

            return null;
        }

        public boolean hasIcon() {
            return icon;
        }
        public boolean hasParticle() {
            return particle;
        }

        public boolean hasCustomParticle() {
            return customParticle;
        }

        public String getParticleType() {
            return particleType;
        }

        public EnumParticleType getCustomParticleType() {
            return customParticleType;
        }

        public double getParticleSpeed() {
            return particleSpeed;
        }
    }
}