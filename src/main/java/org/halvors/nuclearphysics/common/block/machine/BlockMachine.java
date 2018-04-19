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
import org.halvors.nuclearphysics.client.render.block.BlockRenderingHandler;
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
    public void registerIcons(IIconRegister iconRegister) {
        for (EnumMachine type : EnumMachine.values()) {
            if (type.hasIcon()) {
                iconMap.put(type, iconRegister.registerIcon(Reference.PREFIX + type.getName()));
            }
        }

        super.registerIcons(iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        EnumMachine type = EnumMachine.values()[metadata];

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

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (EnumMachine type : EnumMachine.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        final EnumMachine type = EnumMachine.values()[world.getBlockMetadata(x, y, z)];
        final TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileMachine) {
            final TileMachine tileMachine = (TileMachine) tile;

            String particleTypes = null;
            float xRandom = (float) x + 0.5F;
            float yRandom = (float) y + 0.2F + random.nextFloat() * 6.0F / 16.0F;
            float zRandom = (float) z + 0.5F;
            float iRandom = 0.52F;
            float jRandom = random.nextFloat() * 0.6F - 0.3F;
            double xSpeed = 0;
            double ySpeed = 0;
            double zSpeed = 0;

            switch (type) {
                case NUCLEAR_BOILER:
                    if (tileMachine.getOperatingTicks() > 0) {
                        particleTypes = "cloud";
                        ySpeed = 0.05;
                    }
                    break;
            }

            if (particleTypes != null) {
                switch (tileMachine.getFacing()) {
                    case NORTH:
                        world.spawnParticle(particleTypes, (xRandom + jRandom), yRandom, (zRandom - iRandom), xSpeed, ySpeed, zSpeed);
                        break;

                    case SOUTH:
                        world.spawnParticle(particleTypes, (xRandom + jRandom), yRandom, (zRandom + iRandom), xSpeed, ySpeed, zSpeed);
                        break;

                    case WEST:
                        world.spawnParticle(particleTypes, (xRandom - iRandom), yRandom, (zRandom + jRandom), xSpeed, ySpeed, zSpeed);
                        break;

                    case EAST:
                        world.spawnParticle(particleTypes, (xRandom + iRandom), yRandom, (zRandom + jRandom), xSpeed, ySpeed, zSpeed);
                        break;
                }
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileMachine) {
            ((TileMachine) tile).updatePower();
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        world.setBlockMetadataWithNotify(x, y, z, itemStack.getMetadata(), 2);

        super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(x, y, z);
        final ItemStack itemStack = player.getHeldItem();

        if (tile instanceof TilePlasmaHeater) {
            return FluidUtility.playerActivatedFluidItem(world, x, y, z, player, itemStack, ForgeDirection.getOrientation(side));
        } else if (!player.isSneaking()) {
            PlayerUtility.openGui(player, world, x, y, z);

            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileMachine) {
            ((TileMachine) tile).updatePower();
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        EnumMachine type = EnumMachine.values()[metadata];

        return type.getTileAsInstance();
    }

    public enum EnumMachine {
        CHEMICAL_EXTRACTOR("chemical_extractor", TileChemicalExtractor.class),
        GAS_CENTRIFUGE("gas_centrifuge", TileGasCentrifuge.class),
        NUCLEAR_BOILER("nuclear_boiler", TileNuclearBoiler.class),
        PARTICLE_ACCELERATOR("particle_accelerator", TileParticleAccelerator.class, true),
        PLASMA_HEATER("plasma_heater", TilePlasmaHeater.class),
        QUANTUM_ASSEMBLER("quantum_assembler", TileQuantumAssembler.class);

        private String name;
        private Class<? extends TileEntity> tileClass;
        private boolean icon;

        EnumMachine(String name, Class<? extends TileEntity> tileClass) {
            this.name = name;
            this.tileClass = tileClass;
        }

        EnumMachine(String name, Class<? extends TileEntity> tileClass, boolean icon) {
            this(name, tileClass);

            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public Class<? extends TileEntity> getTileClass() {
            return tileClass;
        }

        public boolean hasIcon() {
            return icon;
        }

        public TileEntity getTileAsInstance() {
            try {
                return tileClass.newInstance();
            } catch (Exception e) {
                NuclearPhysics.getLogger().error("Unable to indirectly create tile entity.");
                e.printStackTrace();

                return null;
            }
        }
    }
}