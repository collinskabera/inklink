package inklink;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	
	private long userId;
	
	private List<LineItem> lineItems;
	
	private double total;
	
	public Cart() {
		this.lineItems = new ArrayList<>();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public void total() {
		double total = 0;
		for(int i=0;i<lineItems.size();i++) {
			total+= lineItems.get(i).getTotal();
		}
		this.total = total;
	}

}
