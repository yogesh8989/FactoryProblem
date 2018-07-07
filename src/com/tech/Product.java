package com.tech;

import java.util.ArrayList;
import java.util.List;

import com.tech.material.RawMaterial;

public class Product {
	private List<RawMaterial> bolts = new ArrayList<>(2);
	private RawMaterial machine;
	private boolean canRelease = false;

	public RawMaterial addMaterial(RawMaterial m) {
		boolean isReject = false;
		if(m.getType().equals("bolt")){
			if(bolts.size()<2)
				bolts.add(m);
			else
				isReject = true;
		}else{
			if(machine == null)
				machine=m;
			else
				isReject = true;
		}

		this.setRelease();
		if(isReject)
			return m;
		else
			return null;

	}

	public void setRelease(){
		if(bolts.size()==2 && machine!=null)
			this.canRelease=true;
	}

	public boolean canRelease() {
		return canRelease;
	}

}
