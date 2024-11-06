package lol.aliaga.nuhc.utils;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import java.util.Random;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.Material;

public class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        // Creamos un chunk vacío (sin bloques)
        ChunkData chunk = createChunkData(world);
        return chunk;
    }
}
