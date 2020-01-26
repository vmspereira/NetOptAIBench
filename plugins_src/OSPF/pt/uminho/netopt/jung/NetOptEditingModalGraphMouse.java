package pt.uminho.netopt.jung;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;

public class NetOptEditingModalGraphMouse<V, E> extends EditingModalGraphMouse<V, E> {
	
	public NetOptEditingModalGraphMouse(RenderContext<V, E> rc, Factory<V> vertexFactory, Factory<E> edgeFactory) {
		super(rc, vertexFactory, edgeFactory);
	}

	public void setEditingPlugin(EditingGraphMousePlugin<V, E> plugin) {
		editingPlugin = plugin;
	}

}
