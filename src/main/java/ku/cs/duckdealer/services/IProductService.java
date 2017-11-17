package ku.cs.duckdealer.services;

import ku.cs.duckdealer.models.StockedProduct;

import java.util.ArrayList;

public interface IProductService {

    public ArrayList<StockedProduct> getProducts();

    public void updateProduct(StockedProduct p);

    public void addProduct(StockedProduct p);
}
