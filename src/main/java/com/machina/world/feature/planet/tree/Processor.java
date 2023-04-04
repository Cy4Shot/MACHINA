package com.machina.world.feature.planet.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.machina.util.math.MathUtil;
import com.machina.util.math.OpenSimplex2F;
import com.machina.util.sdf.SDF;
import com.machina.util.sdf.operator.SDFDisplacement;

public class Processor {
	public interface ProcessorModule {
		SDF apply(SDF sdf, Random rand);
	}

	public static final Processor EMPTY = Processor.of();

	List<ProcessorModule> modules = new ArrayList<>();

	public Processor(ProcessorModule[] modules) {
		this.modules.addAll(Arrays.asList(modules));
	}

	public SDF apply(SDF in, Random rand) {
		for (ProcessorModule mod : modules) {
			in = mod.apply(in, rand);
		}
		return in;
	}

	public static Processor of(ProcessorModule... modules) {
		return new Processor(modules);
	}

	public static ProcessorModule noise(float noiseScale, float noiseDisp) {
		return (sdf, rand) -> {
			OpenSimplex2F noise = new OpenSimplex2F(rand.nextLong());
			return new SDFDisplacement().setFunction((vec) -> (float) noise.noise3_Classic(vec.x() * noiseScale,
					vec.y() * noiseScale, vec.z() * noiseScale) * noiseDisp).setSource(sdf);
		};
	}

	public static ProcessorModule disp(float disp) {
		return (sdf, rand) -> {
			return new SDFDisplacement().setFunction((vec) -> MathUtil.randRange(-disp, disp, rand)).setSource(sdf);
		};
	}
}
