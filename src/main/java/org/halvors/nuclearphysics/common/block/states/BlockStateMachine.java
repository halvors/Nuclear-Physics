package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.BlockRenderType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.nuclearphysics.common.type.EnumParticleType;

public class BlockStateMachine extends BlockStateFacing {
    public static final Property<EnumMachine> Property<EnumMachine> TYPE = PropertyEnum.create("type", EnumMachine.class);

    public BlockStateMachine(final BlockMachine block) {
        super(block, TYPE);
    }

    public enum EnumMachine implements IStringSerializable {
        CHEMICAL_EXTRACTOR(TileChemicalExtractor.class, EnumParticleType.RADIOACTIVE),
        GAS_CENTRIFUGE(TileGasCentrifuge.class),
        NUCLEAR_BOILER(TileNuclearBoiler.class, ParticleTypes.CLOUD, 0.025),
        PARTICLE_ACCELERATOR(TileParticleAccelerator.class, BlockRenderType.MODEL),
        PLASMA_HEATER(TilePlasmaHeater.class),
        QUANTUM_ASSEMBLER(TileQuantumAssembler.class);

        private final Class<? extends TileEntity> tileClass;
        private final BlockRenderType blockRenderType;

        private boolean particle;
        private boolean customParticle;
        private ParticleTypes particleType;
        private EnumParticleType customParticleType;
        private double particleSpeed;

        EnumMachine(final Class<? extends TileEntity> tileClass, final BlockRenderType blockRenderType) {
            this.tileClass = tileClass;
            this.blockRenderType = blockRenderType;
        }

        EnumMachine(final Class<? extends TileEntity> tileClass) {
            this(tileClass, BlockRenderType.ENTITYBLOCK_ANIMATED);
        }

        EnumMachine(final Class<? extends TileEntity> tileClass, final ParticleTypes particleType, final double particleSpeed) {
            this(tileClass);

            this.particle = true;
            this.particleType = particleType;
            this.particleSpeed = particleSpeed;
        }

        EnumMachine(final Class<? extends TileEntity> tileClass, final ParticleTypes particleType) {
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

        @Override
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

        public BlockRenderType getBlockRenderType() {
            return blockRenderType;
        }

        public boolean hasParticle() {
            return particle;
        }

        public boolean hasCustomParticle() {
            return customParticle;
        }

        public ParticleTypes getParticleType() {
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