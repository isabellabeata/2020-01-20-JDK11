package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Map<Integer, Artist> idMap;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private List<Artist> vertici;
	private List<Arco> archi;
	private List<Artist> best;
	private double esposizioni;
	
	
	public Model() {
		this.dao= new ArtsmiaDAO();
		this.idMap= new HashMap<Integer, Artist>();
	}
	
	public List<String> popolaCmb(){
		return this.dao.popolaCmb();
	}
	
	public void creaGrafo(String ruolo) {
		this.grafo= new SimpleWeightedGraph<Artist,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.vertici= new LinkedList<Artist>(this.dao.getVertici(ruolo, idMap));
		
		Graphs.addAllVertices(this.grafo, vertici);
		archi=new LinkedList<Arco>(this.dao.getArchi(ruolo, idMap));
		
		for(Arco aa: archi) {
			Graphs.addEdgeWithVertices(this.grafo, aa.getA1(), aa.getA2(), aa.getPeso());
		}
		
	}
	
	public String nVertici() {
		return "Grafo creato!"+"\n"+"#verici: "+ this.grafo.vertexSet().size()+"\n";
	}
	
	public String nArchi() {
		return "#archi: "+ this.grafo.edgeSet().size()+"\n";
	}
	
	public String getConnessi(){
		List<Arco> connessi= new LinkedList<Arco>();
		String s="";
		
		for(Arco a: this.archi) {
			
			connessi.add(a);
		}
		
		Collections.sort(connessi, new ComparatoreDiArchi());
		for(Arco aa: connessi) {
			s+=aa.toString()+"\n";
		}
		
		
		return s;
	}
	
	
	public String calcolaPercorso(Integer id) {
		
		double peso=0;
		String s="";

			if(idMap.get(id)!=null) {
				Artist adiacente=idMap.get(id);
				this.best=new LinkedList<Artist>();
				List<Artist> parziale= new LinkedList<Artist>();
				parziale.add(idMap.get(id));
				best.add(idMap.get(id));
				
				
				ConnectivityInspector<Artist, DefaultWeightedEdge> inspector= new ConnectivityInspector<>(this.grafo);
				Set<Artist> componenteConnessa=inspector.connectedSetOf(idMap.get(id));
				cerca(parziale,componenteConnessa,  adiacente, peso);
				
				for(Artist aa: this.best) {
					s+= this.esposizioni+"\n"+ aa.toString()+"\n";
				}
				
				return s;
			}
			else return "Non hai inserito un identificativo valido";
		
	}

	private void cerca(List<Artist> parziale,Set<Artist> componenteConnessa, Artist adiacente, double peso1) {
		if(parziale.size()>best.size()) {
			this.best= new LinkedList<Artist>(parziale);
			this.esposizioni=peso1;
			return;
		}
		
		for(Artist a: componenteConnessa) {
			DefaultWeightedEdge e= this.grafo.getEdge(a, adiacente);
			double peso= this.grafo.getEdgeWeight(e);
			if(peso1==0 || peso==peso1) {
				peso1=peso;
				parziale.add(a);
				cerca(parziale,componenteConnessa,a,peso1);
				parziale.remove(parziale.size()-1);
			}else if(peso!=peso1) {
				return;
			}
		}
	
	}

}
