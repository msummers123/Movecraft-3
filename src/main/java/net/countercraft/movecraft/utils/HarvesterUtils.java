package net.countercraft.movecraft.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.countercraft.movecraft.async.translation.TranslationTaskData;
import net.countercraft.movecraft.craft.Craft;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author mwkaicz <mwkaicz@gmail.com>
 */
public class HarvesterUtils {
    public static void tryPutToDestroyBox(Craft craft, Material mat, MovecraftLocation loc, List<MovecraftLocation> harvestedBlocks, List<MovecraftLocation> droppedBlocks, List<MovecraftLocation> destroyedBlocks ){
        if(
                mat.equals(Material.DOUBLE_PLANT) 
            || 
                mat.equals(Material.WOODEN_DOOR)
            || 
                mat.equals(Material.IRON_DOOR_BLOCK)
            || 
                mat.equals(Material.BANNER)
        ){
            if (craft.getW().getBlockAt( loc.getX(), loc.getY()+1, loc.getZ() ).getType().equals(mat)){
                MovecraftLocation tmpLoc = loc.translate(0, 1, 0);
                if (!destroyedBlocks.contains(tmpLoc) && !harvestedBlocks.contains(tmpLoc)){
                    destroyedBlocks.add(tmpLoc); 
                }
            }else if (craft.getW().getBlockAt( loc.getX(), loc.getY()-1, loc.getZ() ).getType().equals(mat)){
                MovecraftLocation tmpLoc = loc.translate(0, -1, 0);
                if (!destroyedBlocks.contains(tmpLoc) && !harvestedBlocks.contains(tmpLoc)){
                    destroyedBlocks.add(tmpLoc); 
                }
            }
        }else 
        if(mat.equals(Material.CACTUS) || mat.equals(Material.SUGAR_CANE_BLOCK)){ 
            MovecraftLocation tmpLoc = loc.translate(0, 1, 0);
            Material tmpType = craft.getW().getBlockAt( tmpLoc.getX(), tmpLoc.getY(), tmpLoc.getZ() ).getType();
            while (tmpType.equals(mat)){
                if (!droppedBlocks.contains(tmpLoc) && !harvestedBlocks.contains(tmpLoc)){
                    droppedBlocks.add(tmpLoc); 
                }
                if (!destroyedBlocks.contains(tmpLoc) && !harvestedBlocks.contains(tmpLoc)){
                    destroyedBlocks.add(tmpLoc); 
                }
                tmpLoc = tmpLoc.translate(0, 1, 0);
                tmpType = craft.getW().getBlockAt( tmpLoc.getX(), tmpLoc.getY(), tmpLoc.getZ() ).getType();
            }
        }else 
        if(mat.equals(Material.BED_BLOCK)){
            if (craft.getW().getBlockAt( loc.getX()+1, loc.getY(), loc.getZ() ).getType().equals(mat)){
                MovecraftLocation tmpLoc = loc.translate(1, 0, 0);
                if (!destroyedBlocks.contains(tmpLoc) && !harvestedBlocks.contains(tmpLoc)){
                    destroyedBlocks.add(tmpLoc); 
                }
            }else if (craft.getW().getBlockAt( loc.getX()-1, loc.getY(), loc.getZ() ).getType().equals(mat)){
                MovecraftLocation tmpLoc = loc.translate(-1, 0, 0);
                if (!destroyedBlocks.contains(tmpLoc) && !harvestedBlocks.contains(tmpLoc)){
                    destroyedBlocks.add(tmpLoc); 
                }
            }if (craft.getW().getBlockAt( loc.getX(), loc.getY(), loc.getZ()+1 ).getType().equals(mat)){
                MovecraftLocation tmpLoc = loc.translate(0, 0, 1);
                if (!destroyedBlocks.contains(tmpLoc) && !harvestedBlocks.contains(tmpLoc)){
                    destroyedBlocks.add(tmpLoc); 
                }
            }else if (craft.getW().getBlockAt( loc.getX(), loc.getY(), loc.getZ()-1 ).getType().equals(mat)){
                MovecraftLocation tmpLoc = loc.translate(0, 0, -1);
                if (!destroyedBlocks.contains(tmpLoc) && !harvestedBlocks.contains(tmpLoc)){
                    destroyedBlocks.add(tmpLoc); 
                }
            }
        }
        //clear from previous because now it is in harvest
        if (destroyedBlocks.contains(loc)){
            destroyedBlocks.remove(loc);
        }
        if (droppedBlocks.contains(loc)){
            droppedBlocks.remove(loc);
        }
    }
    
    public static ItemStack putInToChests(ItemStack stack, HashMap<Material, ArrayList<Block>> chests){
        if (stack == null) {return null;}
        if (chests == null){return stack;}
        if (chests.isEmpty()){return stack;}
        
        Material mat = stack.getType();
        ItemStack retStack = null;
        
        if (chests.get(mat) != null){
            for (Block b : chests.get(mat)) {
                Inventory inv = ((InventoryHolder) b.getState()).getInventory();
                HashMap<Integer,ItemStack> leftover= inv.addItem(stack); //try add stack to the chest inventory
                if (leftover != null) {
                    ArrayList<Block> blocks = chests.get(mat);
                    if (blocks == null){
                        blocks = new ArrayList<Block>();
                    }
                    
                    if (blocks.size() > 0){
                        chests.put(mat, blocks); //restore chests array in HashMap
                    }else if (chests.get(mat) == null){
                        if (inv.firstEmpty() == -1){
                            chests.remove(mat); //remove  array of chests with this material 
                        }
                        
                    }
                    if (leftover.isEmpty()) {return null;}
                    for (int i=0; i < leftover.size(); i++) {
                        stack = leftover.get(i);
                        break;
                    }
                }else{
                    return null;
                }
            }
        }
        
        mat = Material.AIR;
        if (chests.get(mat) != null){
            for (Block b : chests.get(mat)) {
                Inventory inv = ((InventoryHolder) b.getState()).getInventory();
                HashMap<Integer,ItemStack> leftover= inv.addItem(stack);
                if (leftover != null && !leftover.isEmpty()) {
                    stack = null;
                    ArrayList<Block> blocks = chests.get(mat);
                    if (blocks == null){
                        blocks = new ArrayList<Block>();
                    }
                    if (blocks.size() > 0){
                        if (inv.firstEmpty() != -1){
                            chests.put(mat, blocks);
                        }
                    }else if (chests.get(mat) == null){
                        if (inv.firstEmpty() == -1){
                            chests.remove(mat); //remove  array of chests with this material 
                        }
                    }
                    for (int i=0; i < leftover.size(); i++) {
                        return leftover.get(i);
                    }   
                }else{
                    //create new stack for this material
                    if (stack != null){
                        Material newMat = stack.getType();
                        ArrayList<Block> newBlocks = chests.get(newMat);
                        if (newBlocks == null) {
                            newBlocks = new ArrayList<Block>();
                        }
                        newBlocks.add(b);
                        chests.put(newMat, newBlocks);
                    }
                    return null;
                }
            }
        }  
        
        return stack;
    }
    
    public static void captureYield(Craft craft, TranslationTaskData data, MovecraftLocation[] blocksList, List<MovecraftLocation> harvestedBlocks, List<MovecraftLocation> droppedBlocks){
        boolean canDrop = craft.getType().getHarvesterCanDropItems();
        boolean canStore = craft.getType().getHarvesterCanStoreItems();
        
        if (harvestedBlocks.isEmpty() || (!canDrop && !canStore)){return;}
        
        HashMap<Material, ArrayList<Block>> crates = new HashMap<Material,ArrayList<Block>>();
        HashSet<ItemDropUpdateCommand> itemDropUpdateSet = new HashSet<ItemDropUpdateCommand>();
        HashSet<Material> droppedSet = new HashSet<Material>();
        HashMap<MovecraftLocation, ItemStack[]> droppedMap = new HashMap<MovecraftLocation, ItemStack[]>();
        harvestedBlocks.addAll(droppedBlocks);
        
        ItemStack retStack;
        boolean oSomethingToDrop, oWheat;
        for (MovecraftLocation harvestedBlock : harvestedBlocks) {
            Block block = craft.getW().getBlockAt( harvestedBlock.getX(), harvestedBlock.getY(), harvestedBlock.getZ());
            ItemStack[] drops = block.getDrops().toArray(new ItemStack[block.getDrops().size()]);
            oSomethingToDrop = false;
            oWheat = false;
            for (ItemStack drop : drops) {
                if (drop != null){
                    oSomethingToDrop = true;
                    if (!droppedSet.contains(drop.getType())){
                        droppedSet.add(drop.getType());
                    }
                    if (drop.getType() == Material.WHEAT){
                        oWheat = true;
                    }
                }
            }
            if (oWheat){
                Random rand = new Random();
                int amount = rand.nextInt(4);
                if (amount > 0){
                    ItemStack seeds = new ItemStack(Material.SEEDS, amount);
                    HashSet<ItemStack> d = new HashSet<ItemStack>(Arrays.asList(drops));
                    d.add(seeds);
                    drops = d.toArray(new ItemStack[d.size()]);
                }
            }
            if (drops.length > 0 && oSomethingToDrop){
                droppedMap.put(harvestedBlock, drops);
            }
        }
        
        //find chests
        if (canStore){
            for (MovecraftLocation bTest : blocksList) {
                Block b= craft.getW().getBlockAt(bTest.getX(), bTest.getY(), bTest.getZ());
                if(b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
                    Inventory inv = ((InventoryHolder) b.getState()).getInventory();
                    //get chests with dropped Items
                    for (Material mat: droppedSet){
                        for (Map.Entry<Integer, ? extends ItemStack> pair : ((HashMap<Integer, ? extends ItemStack>)inv.all(mat)).entrySet()){
                            ItemStack stack = (ItemStack) pair.getValue();
                            if (stack.getAmount() < stack.getMaxStackSize() || inv.firstEmpty() > -1){    
                                ArrayList<Block> blocks = crates.get(mat);
                                if (blocks == null) {blocks = new ArrayList<Block>();}
                                if (blocks.contains(b)){} else {blocks.add(b);}
                                crates.put(mat, blocks);
                            }
                        }
                    }
                    // get chests with free slots
                    if (inv.firstEmpty() != -1){
                        Material mat = Material.AIR;
                        ArrayList<Block> blocks = crates.get(mat);
                        if (blocks == null) {blocks = new ArrayList<Block>();}
                        if (!blocks.contains(b)){blocks.add(b);}
                        crates.put(mat, blocks);
                    }
                }
            }
        }
        
        for (MovecraftLocation harvestedBlock : harvestedBlocks) {
            if (droppedMap.containsKey(harvestedBlock)){
                ItemStack[] drops = droppedMap.get(harvestedBlock);
                for (ItemStack drop : drops) {
                    if (!droppedBlocks.contains(harvestedBlock)  && canStore){
                        retStack = putInToChests(drop, crates);
                    }else{
                        retStack = drop;
                    }
                    if (retStack != null && canDrop){
                        //drop items on position 
                        Location loc = new Location(craft.getW(), harvestedBlock.getX(), harvestedBlock.getY(), harvestedBlock.getZ());
                        ItemDropUpdateCommand iUp = new ItemDropUpdateCommand(loc ,drop);
                        itemDropUpdateSet.add(iUp);
                    }
                }
            }
        }
        if (itemDropUpdateSet.size() > 0)
            data.setItemDropUpdates(itemDropUpdateSet.toArray( new ItemDropUpdateCommand[1] ));
    }
}
