package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import org.halvors.nuclearphysics.client.render.particle.EnumParticleType;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;

public class BlockStateMachine extends BlockStateFacing {
    public static final PropertyEnum<EnumMachine> TYPE = PropertyEnum.create("type", EnumMachine.class);

    public BlockStateMachine(final BlockMachine block) {
        super(block, TYPE);
    }

    public enum EnumMachine implements IStringSerializable {
        CHEMICAL_EXTRACTOR(TileChemicalExtractor.class, EnumParticleType.RADIOACTIVE),
        GAS_CENTRIFUGE(TileGasCentrifuge.class),
        NUCLEAR_BOILER(TileNuclearBoiler.class, EnumParticleTypes.CLOUD, 0.025),
        PARTICLE_ACCELERATOR(TileParticleAccelerator.class, EnumBlockRenderType.MODEL),
        PLASMA_HEATER(TilePlasmaHeater.class),
        QUANTUM_ASSEMBLER(TileQuantumAssembler.class);

        private final Class<? extends TileEntity> tileClass;
        private final EnumBlockRenderType renderType;

        private boolean particle;
        private boolean customParticle;
        private EnumParticleTypes particleType;
        private EnumParticleType customParticleType;
        private double particleSpeed;

        EnumMachine(final Class<? extends TileEntity> tileClass, final EnumBlockRenderType renderType) {
            this.tileClass = tileClass;
            this.renderType = renderType;
        }

        EnumMachine(final Class<? extends TileEntity> tileClass) {
            this(tileClass, EnumBlockRenderType.ENTITYBLOCK_ANIMATED);
        }

        EnumMachine(final Class<? extends TileEntity> tileClass, final EnumParticleTypes particleType, final double particleSpeed) {
            this(tileClass);

            this.particle = true;
            this.particleType = particleType;
            this.particleSpeed = particleSpeed;
        }

        EnumMachine(final Class<? extends TileEntity> tileClass, final EnumParticleTypes particleType) {
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

        public boolean hasParticle() {
            return particle;
        }

        public boolean hasCustomParticle() {
            return customParticle;
        }

        public EnumParticleTypes getParticleType() {
            return particleType;
        }

        public EnumParticleType getCustomParticleType() {
            return customParticleType;
        }

        public double getParticleSpeed() {
            return particleSpeed;
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

        public EnumBlockRenderType getRenderType() {
            return renderType;
        }
    }
}