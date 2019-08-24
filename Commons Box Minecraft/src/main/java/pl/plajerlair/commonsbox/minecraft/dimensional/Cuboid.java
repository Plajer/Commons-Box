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

  private final int xMin;
  private final int xMax;
  private final int yMin;
  private final int yMax;
  private final int zMin;
  private final int zMax;
  private final double xMinCentered;
  private final double xMaxCentered;
  private final double yMinCentered;
  private final double yMaxCentered;
  private final double zMinCentered;
  private final double zMaxCentered;
  private final World world;

  public Cuboid(final Location point1, final Location point2) {
    this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
    this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
    this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
    this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
    this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
    this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
    this.world = point1.getWorld();
    this.xMinCentered = this.xMin + 0.5;
    this.xMaxCentered = this.xMax + 0.5;
    this.yMinCentered = this.yMin + 0.5;
    this.yMaxCentered = this.yMax + 0.5;
    this.zMinCentered = this.zMin + 0.5;
    this.zMaxCentered = this.zMax + 0.5;
  }

  public List<Block> blockList() {
    final List<Block> bL = new ArrayList<>(this.getTotalBlockSize());
    for (int x = this.xMin; x <= this.xMax; ++x) {
      for (int y = this.yMin; y <= this.yMax; ++y) {
        for (int z = this.zMin; z <= this.zMax; ++z) {
          final Block b = this.world.getBlockAt(x, y, z);
          bL.add(b);
        }
      }
    }
    return bL;
  }

  public List<Block> blockListWithoutFloor() {
    final List<Block> bL = new ArrayList<>(this.getTotalBlockSize() - (this.getXWidth() * this.getZWidth()));
    for (int x = this.xMin; x <= this.xMax; ++x) {
      for (int y = this.yMin + 1; y <= this.yMax; ++y) {
        for (int z = this.zMin; z <= this.zMax; ++z) {
          final Block b = this.world.getBlockAt(x, y, z);
          bL.add(b);
        }
      }
    }
    return bL;
  }

  public List<Block> floorBlockList() {
    final List<Block> bL = new ArrayList<>(this.getXWidth() * this.getZWidth());
    for (int x = this.xMin; x <= this.xMax; ++x) {
      for (int z = this.zMin; z <= this.zMax; ++z) {
        final Block b = this.world.getBlockAt(x, this.yMin, z);
        bL.add(b);
      }
    }
    return bL;
  }

  public List<Chunk> chunkList() {
    final List<Chunk> chunks = new ArrayList<>();
    for (Block block : blockList()) {
      if (chunks.contains(block.getChunk())) {
        continue;
      }
      chunks.add(block.getChunk());
    }
    return chunks;
  }

  public Location getCenter() {
    return new Location(this.world, (this.xMax - this.xMin) / 2 + this.xMin, (this.yMax - this.yMin) / 2 + this.yMin, (this.zMax - this.zMin) / 2 + this.zMin);
  }

  public double getDistance() {
    return this.getMinPoint().distance(this.getMaxPoint());
  }

  public double getDistanceSquared() {
    return this.getMinPoint().distanceSquared(this.getMaxPoint());
  }

  public int getHeight() {
    return this.yMax - this.yMin + 1;
  }

  public Location getMinPoint() {
    return new Location(this.world, this.xMin, this.yMin, this.zMin);
  }

  public Location getMaxPoint() {
    return new Location(this.world, this.xMax, this.yMax, this.zMax);
  }

  public Location getRandomLocation() {
    final Random rand = new Random();
    final int x = rand.nextInt(Math.abs(this.xMax - this.xMin) + 1) + this.xMin;
    final int y = rand.nextInt(Math.abs(this.yMax - this.yMin) + 1) + this.yMin;
    final int z = rand.nextInt(Math.abs(this.zMax - this.zMin) + 1) + this.zMin;
    return new Location(this.world, x, y, z);
  }

  public int getTotalBlockSize() {
    return this.getHeight() * this.getXWidth() * this.getZWidth();
  }

  public int getXWidth() {
    return this.xMax - this.xMin + 1;
  }

  public int getZWidth() {
    return this.zMax - this.zMin + 1;
  }

  public boolean isIn(final Location loc) {
    return loc.getWorld() == this.world && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax && loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax && loc
        .getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
  }

  public boolean isIn(final Player player) {
    return this.isIn(player.getLocation());
  }

  public boolean isInWithMarge(final Location loc, final double marge) {
    return loc.getWorld() == this.world && loc.getX() >= this.xMinCentered - marge && loc.getX() <= this.xMaxCentered + marge && loc.getY() >= this.yMinCentered - marge && loc
        .getY() <= this.yMaxCentered + marge && loc.getZ() >= this.zMinCentered - marge && loc.getZ() <= this.zMaxCentered + marge;
  }

  public boolean isEmpty() {
    for (Block block : this.blockList()) {
      if (block.getType() != Material.AIR) {
        return false;
      }
    }
    return true;
  }

  public boolean contains(final Material material) {
    for (Block block : this.blockList()) {
      if (block.getType() == material) {
        return true;
      }
    }
    return false;
  }

  public void fill(final Material material) {
    for (Block block : this.blockList()) {
      block.setType(material);
    }
  }

  public void fillWithoutFloor(final Material material) {
    for (Block block : this.blockListWithoutFloor()) {
      block.setType(material);
    }
  }

  public void fillFloor(final Material material){
    for (Block block : this.floorBlockList()) {
      block.setType(material);
    }
  }

  public boolean collidesWith(final Cuboid other) {
    if (this.xMax < other.xMin || this.xMin > other.xMax) {
      return false;
    }
    if (this.yMax < other.yMin || this.yMin > other.yMax) {
      return false;
    }
    if (this.zMax < other.zMin || this.zMin > other.zMax) {
      return false;
    }
    return true;
  }

  public static boolean collidesWith(final Cuboid left, final Cuboid right) {
    return left.collidesWith(right);
  }
}
