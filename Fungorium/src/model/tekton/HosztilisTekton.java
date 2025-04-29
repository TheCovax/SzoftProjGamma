package Fungorium.src.model.tekton;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HosztilisTekton extends Tekton {

	double killRate;

    /**
	 * splitRate es gombaTest beallito konstruktor
	 * @param splitR az esely amivel eltorik a tekton
	 * @param neighborsList a tekton szomszedjainak listaja
	 */
	public HosztilisTekton(double splitR, List<Tekton> neighborsList){
		super(splitR, neighborsList);
	}

    public HosztilisTekton(){
		super();
	}

	public HosztilisTekton(String id){
		super(id);
	}

	/**
	 * Felulirja az ososztaly fuggvenyet,
	 * a szettores mellett azt is ellenorzi, hogy megolje-e a rajta levo gombatestet
	 */
	@Override
	public void update(){
		if(canSplit()) split();
		if(canKill()) kill();
	}

	/**
	 * Megadja hogy a korben megolje-e a gombatestet
	 */
	boolean canKill(){
		double kill = ThreadLocalRandom.current().nextDouble(0, 1);

		//Ha a kill erteke kisebb mint a killRate megoli, egyebkent noveli a kill ratet
		if(kill <= killRate) return true;
		else {
			killRate += 0.1;
			return false;
		} 
	}

	//Megoli a gombatestet
	void kill(){
		//test.clear();
	}
}
