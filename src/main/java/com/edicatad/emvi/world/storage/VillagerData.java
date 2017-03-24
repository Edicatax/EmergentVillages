package com.edicatad.emvi.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class VillagerData extends WorldSavedData{
	private NBTTagCompound data = new NBTTagCompound();
	private int dimension;
	
	public VillagerData(String tagName){
		super(tagName);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound) {
		data = compound.getCompoundTag("EmVi");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	compound.setTag("EmVi", data);
    	return compound;
    }
    
    public NBTTagCompound getData() {
    	return data;
    }
    
    public void setDimensionID(int dimensionID){
    	dimension = dimensionID;
    }
    
    public int getDimensionID(){
    	return dimension;
    }
}
