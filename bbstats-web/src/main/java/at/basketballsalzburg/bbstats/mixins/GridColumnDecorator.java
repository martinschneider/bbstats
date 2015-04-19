package at.basketballsalzburg.bbstats.mixins;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;

/**
 * Applies additional CSS classes to grid rows
 */
@MixinAfter
public class GridColumnDecorator {
	@InjectContainer
	private Grid grid;

	@Parameter(allowNull = false)
	private Map<String, String> cssClassMap;

	@AfterRender
	void afterRender(MarkupWriter writer) {
		List<Node> topChildren = writer.getElement().getChildren();
		Object containingDivObject = topChildren.get(topChildren.size() - 1);
		if (containingDivObject instanceof Element) {
			Element containingDiv = (Element) containingDivObject;
			decorateRows(containingDiv.find("thead").getChildren());
			decorateRows(containingDiv.find("tbody").getChildren());
		}
	}

	private void decorateRows(List<Node> rows) {
		List<String> propertyNames = grid.getDataModel().getPropertyNames();
		for (Node rowNode : rows) {
			Element rowElement = (Element) rowNode;
			List<Node> cells = rowElement.getChildren();
			int tdIndex = 0;
			for (String propertyName : propertyNames) {
				Element cellElement = (Element) cells.get(tdIndex);
				if (cssClassMap.containsKey(propertyName)) {
					cellElement.attribute("class",
							cssClassMap.get(propertyName));
				}
				++tdIndex;
			}
		}
	}
}
