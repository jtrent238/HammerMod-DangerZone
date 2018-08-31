package dangerzone.entities;
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

import org.newdawn.slick.opengl.Texture;

import dangerzone.DamageTypes;
import dangerzone.InventoryContainer;
import dangerzone.Player;
import dangerzone.TextureMapper;
import dangerzone.Utils;
import dangerzone.World;
import dangerzone.items.Items;


public class Vampire extends EntityLiving {

	public static int lightcount = 0;
	
	public Vampire(World w) {
		super(w);
		width = 0.75f;
		height = 1.75f;	
		uniquename = "DangerZone:Vampire";		
		has_inventory = true;
		attackRange = 3.0f;
		setMaxHealth(60);
		setHealth(60);
		setDefense(1.5f);
		setAttackDamage(10);
		setExperience(80);
		setMaxAir(40);
		setAir(40);
		temperament = Temperament.HOSTILE;
		enable_hostile = true;
		daytimespawn = false;
		daytimedespawn = true;
		nighttimespawn = true;
		nighttimedespawn = false;
		tower_defense_enable = true;
	}

	/*
	 * Initialize our armor once and only once!
	 */
	public void init(){
		super.init();
		if(world != null){
			if(world.isServer){
				if(getInitialized() == 0){
					setInitialized(1); //we are now!
					/*
					 * Some weapons and armor are possible...
					 */
					if(world.rand.nextInt(6) == 0){
						int wt = world.rand.nextInt(4);
						int itd = 0;
						InventoryContainer ic = new InventoryContainer();
						if(wt == 0)itd = Items.stoneaxe.itemID;
						if(wt == 1)itd = Items.woodenaxe.itemID;
						if(wt == 2)itd = Items.stonesword.itemID;
						if(wt == 3)itd = Items.woodensword.itemID;
						ic.iid = itd;
						ic.count = 1;
						ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
						setHotbar(0, ic);
						sethotbarindex(0);
						if(world.rand.nextInt(2) == 0){
							ic = new InventoryContainer();
							ic.iid = Items.tinhelmet.itemID;
							ic.count = 1;
							ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
							setArmor(0, ic);
						}
						if(world.rand.nextInt(2) == 0){
							ic = new InventoryContainer();
							ic.iid = Items.tinchestplate.itemID;
							ic.count = 1;
							ic.currentuses = world.rand.nextInt(ic.getItem().maxuses);
							setArmor(1, ic);
						}
					}										
				}				
			}
		}
	}
	
	public void update(float deltaT){
		if(this.world.isServer){
			lightcount++;
			if(world.isDaytime() && lightcount > 100){ //don't check lighting too often. asks client across network!
				lightcount = 0;
				if(getLightAtLocation(world, dimension, (int)posx, (int)posy, (int)posz) > 0.55f){
					setOnFire(50);
					if(world.rand.nextInt(20) == 0){
						doAttackFrom(null, DamageTypes.DAYTIME, 1f);
					}
				}
			}
		}
		super.update(deltaT);
	}
	
	public String getLivingSound(){
		if(world.rand.nextInt(5) != 0)return null;
		return "DangerZone:vampire_living";
	}
	
	public String getHurtSound(){
		return "DangerZone:werewolf_attack";
	}
	
	public String getDeathSound(){
		return "DangerZone:werewolf_death";
	}
	
	public String getAttackSound(){
		return "DangerZone:werewolf_attack";
	}
	
	public void doDeathDrops(){		
		super.doDeathDrops();
		if(world.rand.nextInt(10)==1)Utils.doDropRand(world, 0, Items.trophyvampire.itemID, 1f, dimension, posx, posy, posz);
		Utils.doDropRand(world, 0, Items.vampireteeth.itemID, 2f, dimension, posx, posy, posz);
	}
	
	public boolean isSuitableTarget(Entity e){
		if(isIgnorable(e))return false;
		if(e instanceof TheCount)return false;
		if(e instanceof Vampire)return false;
		if(e instanceof VampireMoose)return false;
		if(e.temperament == Temperament.HOSTILE && CanProbablySeeEntity(e) )return true;
		if(e instanceof Player && CanProbablySeeEntity(e) )return true;
		return false;
	}
	
	//Model calls back out to see what texture to use.
	public Texture getTexture(){
		if(texture == null){
			//ENTITIES MUST USE TEXTUREMAPPER.GETTEXTURE()!!!!
			texture = TextureMapper.getTexture("res/skins/"+ "vampire.png");	//this is not fast, so we keep our own pointer!
		}
		return texture;
	}

}