package demo;

public class ColumnInfo {

	private String _value;
	private String _label;
	
	private boolean _visible;
	
	private String _templateName;
	
	public ColumnInfo(String value, String label, boolean visible, String templateName) {
		_value = value;
		_label = label;
		_visible = visible;
		_templateName = templateName;
	}
	
	public String getValue() {
		return _value;
	}

	public String getLabel() {
		return _label;
	}

	public void setLabel(String label) {
		this._label = label;
	}
	
	public boolean isVisible() {
		return _visible;
	}

	public void setVisible(boolean _visible) {
		this._visible = _visible;
	}

	public String getTemplateName() {
		return _templateName;
	}

	public void setTemplateName(String name) {
		_templateName = name;
	}
}