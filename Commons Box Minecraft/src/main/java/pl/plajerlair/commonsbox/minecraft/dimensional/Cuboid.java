package pl.plajerlair.commonsbox.minecraft.dimensional;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Cuboid {

  private final int xMin, xMax, yMin, yMax, zMin, zMax;
  private final double xMinCentered, xMaxCentered, yMinCentered, yMaxCentered, zMinCentered, zMaxCentered;
  private final World world;

  public Cuboid(final Location point1, final Location point2) {
    xMin = Math.min(point1.getBlockX(), point2.getBlockX());
    xMax = Math.max(point1.getBlockX(), point2.getBlockX());
    yMin = Math.min(point1.getBlockY(), point2.getBlockY());
    yMax = Math.max(point1.getBlockY(), point2.getBlockY());
    zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
    zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
    world = point1.getWorld();
    xMinCentered = xMin + 0.5;
    xMaxCentered = xMax + 0.5;
    yMinCentered = yMin + 0.5;
    yMaxCentered = yMax + 0.5;
    zMinCentered = zMin + 0.5;
    zMaxCentered = zMax + 0.5;
  }

  public List<Block> blockList() {
    final List<Block> bL = new ArrayList<>(getTotalBlockSize());
    for (int x = xMin; x <= xMax; ++x) {
      for (int y = yMin; y <= yMax; ++y) {
        for (int z = zMin; z <= zMax; ++z) {
          final Block b = world.getBlockAt(x, y, z);
          bL.add(b);
        }
      }
    }
    return bL;
  }

  public List<Block> blockListWithoutFloor() {
    final List<Block> bL = new ArrayList<>(getTotalBlockSize() - (getXWidth() * getZWidth()));
    for (int x = xMin; x <= xMax; ++x) {
      for (int y = yMin + 1; y <= yMax; ++y) {
        for (int z = zMin; z <= zMax; ++z) {
          final Block b = world.getBlockAt(x, y, z);
          bL.add(b);
        }
      }
    }
    return bL;
  }

  public List<Block> floorBlockList() {
    final List<Block> bL = new ArrayList<>(getXWidth() * getZWidth());
    for (int x = xMin; x <= xMax; ++x) {
      for (int z = zMin; z <= zMax; ++z) {
        final Block b = world.getBlockAt(x, yMin, z);
        bL.add(b);
      }
    }
    return bL;
  }

  public List<Chunk> chunkList() {
    final List<Chunk> chunks = new ArrayList<>();
    for (Block block : blockList()) {
      if (!chunks.contains(block.getChunk())) {
        chunks.add(block.getChunk());
      }
    }
    return chunks;
  }

  public Location getCenter() {
    return new Location(world, (xMax - xMin) / 2 + xMin, (yMax - yMin) / 2 + yMin, (zMax - zMin) / 2 + zMin);
  }

  public double getDistance() {
    return getMinPoint().distance(getMaxPoint());
  }

  public double getDistanceSquared() {
    return getMinPoint().distanceSquared(getMaxPoint());
  }

  public int getHeight() {
    return yMax - yMin + 1;
  }

  public Location getMinPoint() {
    return new Location(world, xMin, yMin, zMin);
  }

  public Location getMaxPoint() {
    return new Location(world, xMax, yMax, zMax);
  }

  public Location getRandomLocation() {
    final Random rand = new Random();
    final int x = rand.nextInt(Math.abs(xMax - xMin) + 1) + xMin;
    final int y = rand.nextInt(Math.abs(yMax - yMin) + 1) + yMin;
    final int z = rand.nextInt(Math.abs(zMax - zMin) + 1) + zMin;
    return new Location(world, x, y, z);
  }

  public int getTotalBlockSize() {
    return getHeight() * getXWidth() * getZWidth();
  }

  public int getXWidth() {
    return xMax - xMin + 1;
  }

  public int getZWidth() {
    return zMax - zMin + 1;
  }

  public boolean isIn(final Location loc) {
    return loc.getWorld() == world && loc.getBlockX() >= xMin && loc.getBlockX() <= xMax && loc.getBlockY() >= yMin && loc.getBlockY() <= yMax && loc
        .getBlockZ() >= zMin && loc.getBlockZ() <= zMax;
  }

  public boolean isIn(final Player player) {
    return isIn(player.getLocation());
  }

  public boolean isInWithMarge(final Location loc, final double marge) {
    return loc.getWorld() == world && loc.getX() >= xMinCentered - marge && loc.getX() <= xMaxCentered + marge && loc.getY() >= yMinCentered - marge && loc
        .getY() <= yMaxCentered + marge && loc.getZ() >= zMinCentered - marge && loc.getZ() <= zMaxCentered + marge;
  }

  public boolean isEmpty() {
    for (Block block : blockList()) {
      if (block.getType() != Material.AIR) {
        return false;
      }
    }
    return true;
  }

  public boolean contains(final Material material) {
    for (Block block : blockList()) {
      if (block.getType() == material) {
        return true;
      }
    }
    return false;
  }

  public void fill(final Material material) {
    for (Block block : blockList()) {
      block.setType(material);
    }
  }

  public void fillWithoutFloor(final Material material) {
    for (Block block : blockListWithoutFloor()) {
      block.setType(material);
    }
  }

  public void fillFloor(final Material material){
    for (Block block : floorBlockList()) {
      block.setType(material);
    }
  }

  public boolean collidesWith(final Cuboid other) {
    if (xMax < other.xMin || xMin > other.xMax) {
      return false;
    }
    if (yMax < other.yMin || yMin > other.yMax) {
      return false;
    }
    if (zMax < other.zMin || zMin > other.zMax) {
      return false;
    }
    return true;
  }

  public static boolean collidesWith(final Cuboid left, final Cuboid right) {
    return left.collidesWith(right);
  }
}
