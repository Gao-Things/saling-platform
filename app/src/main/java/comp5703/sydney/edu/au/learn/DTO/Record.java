package comp5703.sydney.edu.au.learn.DTO;

import java.util.List;

public class Record {
    private Product product;
    private List<Long> priceUpdateTime;
    private List<Double> priceUpdateRecord;

    public Record() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Long> getPriceUpdateTime() {
        return priceUpdateTime;
    }

    public void setPriceUpdateTime(List<Long> priceUpdateTime) {
        this.priceUpdateTime = priceUpdateTime;
    }

    public List<Double> getPriceUpdateRecord() {
        return priceUpdateRecord;
    }

    public void setPriceUpdateRecord(List<Double> priceUpdateRecord) {
        this.priceUpdateRecord = priceUpdateRecord;
    }
}
