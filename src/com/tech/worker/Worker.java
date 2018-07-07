package com.tech.worker;

import java.util.concurrent.BlockingQueue;

import com.tech.Factory;
import com.tech.Product;
import com.tech.material.RawMaterial;

public class Worker implements Runnable {

	private BlockingQueue<RawMaterial> belt;
	int timeToBuildProd;
	Product p;
	public Worker(BlockingQueue<RawMaterial> belt, int timeToBuildProd){
		this.belt=belt;
		this.timeToBuildProd = timeToBuildProd;
	}

	@Override
	public void run() {
		p= new Product();
		while(!belt.isEmpty()){
			try {
				RawMaterial m = belt.take();
				m = p.addMaterial(m);
				if(m!=null){
					belt.offer(m);
				}
				if( p.canRelease()){
					Thread.currentThread().sleep(timeToBuildProd);
					Factory.prodList.add(p);
					p= new Product();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
