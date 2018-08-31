package dangerzone.items;
import dangerzone.Player;
import dangerzone.entities.Entity;
import dangerzone.gui.InventoryMenus;

/*
 * This code is copyright Richard H. Clark, TheyCallMeDanger, OreSpawn, 2015-2020.
 * You may use this code for reference for modding the DangerZone game program,
 * and are perfectly welcome to cut'n'paste portions for your mod as well.
 * DO NOT USE THIS CODE FOR ANY PURPOSE OTHER THAN MODDING FOR THE DANGERZONE GAME.
 * DO NOT REDISTRIBUTE THIS CODE. 
 * 
 * This copyright remains in effect until January 1st, 2021. 
 * At that time, this code becomes public domain.
 * 
 * WARNING: There are bugs. Big bugs. Little bugs. Every size in-between bugs.
 * This code is NOT suitable for use in anything other than this particular game. 
 * NO GUARANTEES of any sort are given, either express or implied, and Richard H. Clark, 
 * TheyCallMeDanger, OreSpawn are not responsible for any damages, direct, indirect, or otherwise. 
 * You should have made backups. It's your own fault for not making them.
 * 
 * NO ATTEMPT AT SECURITY IS MADE. This code is USE AT YOUR OWN RISK.
 * Regardless of what you may think, the reality is, that the moment you 
 * connected your computer to the Internet, Uncle Sam, among many others, hacked it.
 * DO NOT KEEP VALUABLE INFORMATION ON INTERNET-CONNECTED COMPUTERS.
 * Or your phone...
 * 
 */
public class ItemTrophy extends Item {
	
	public String critter;
	public float scale = 1.0f;
	
	public ItemTrophy(String name, String tospawn, float ss, int mxsck){
		super(name, "res/items/trophy.png");
		critter = tospawn;
		scale = ss;
		maxstack = mxsck;
		menu = InventoryMenus.TROPHY;
	}
		
	//Player right-clicked on this block with this item
	//called from client side.
	public boolean rightClickOnBlock(Player p, int dimension, int x, int y, int z, int side){
		if(p != null && p.world.isServer && side == 0){
			Entity eb = p.world.createEntityByName("DangerZone:EntityTrophy", dimension, ((double)x)+0.5f, ((double)y)+1.05f, ((double)z)+0.5f);
			if(eb != null){
				eb.init();
				eb.setVarString(0, critter);
				eb.setIID(this.itemID);
				eb.setVarFloat(0, scale);
				p.world.spawnEntityInWorld(eb);
				return true; //delete me, I'm done!
			}
		}
		return false; 
	}

}