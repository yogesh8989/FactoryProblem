package com.tech;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.tech.material.Bolt;
import com.tech.material.Machine;
import com.tech.material.RawMaterial;
import com.tech.worker.Worker;

public class Factory {
	
	public static List<Product> prodList = new CopyOnWriteArrayList<>();
	
	public static void main(String[] args) {
		System.out.println("Number of product in factory befor start:"+prodList.size());
		new Factory().buildProducts(10, 5, 60000);
		printNumberOfProd();
	}

	public void buildProducts(int bolts, int machines, int timeToBuildProd) {
		if(bolts != machines*2){
			throw new InvalidParameterException();
		}
		BlockingQueue<RawMaterial> belt = createBelt(bolts, machines);
		Worker w1 = null;
		List<Future> fl = new ArrayList<>();
		ExecutorService tp = Executors.newFixedThreadPool(3);
		Date d1 = new Date();
		for(int i=0;i<3;i++){
			w1 = new Worker(belt, timeToBuildProd);
			Future<?> f = tp.submit(w1);
			fl.add(f);
		}
		tp.shutdown();
		for(Future f:fl){
			try {
				Object o = f.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Date d2 = new Date();
		long duration = d2.getTime() - d1.getTime();
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMillSeconds = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println("Time taken to build Products -> \n Seconds : "+diffInSeconds+"\n MilliSeconds : "+diffInMillSeconds);
	}

	private static void printNumberOfProd() {
		System.out.println("Number of products in factory:"+prodList.size());
		
	}

	private BlockingQueue<RawMaterial> createBelt(int bolts, int machines) {
		BlockingQueue<RawMaterial> belt = new ArrayBlockingQueue<>((bolts+machines));
		RawMaterial m ;
		for(int i=0;i<machines;i++){
			m=new Machine("machine");
			belt.add(m);
		}
		for(int i=0;i<bolts;i++){
			m=new Bolt("bolt");
			belt.add(m);
		}
		return belt;
	}

}
