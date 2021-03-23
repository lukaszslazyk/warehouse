package studia.ium.warehouse.application.rest.product.control;

import studia.ium.warehouse.business.product.entity.ProductOperation;
import studia.ium.warehouse.business.product.entity.ProductOperationType;
import studia.ium.warehouse.business.product.entity.ProductPatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class SynchronizeInputParser {
    public List<ProductOperation> parse(List<ProductOperation> productOperations) {
        return productOperations.stream()
            .map(this::parseSingle)
            .collect(Collectors.toList());
    }

    private ProductOperation parseSingle(ProductOperation productOperation) {
        if (isOfEditType(productOperation) && productIsSetIn(productOperation))
            return parseEditOperation(productOperation);
        else
            return productOperation;
    }

    private boolean isOfEditType(ProductOperation productOperation) {
        return productOperation.getType().equals(ProductOperationType.EDIT);
    }

    private boolean productIsSetIn(ProductOperation productOperation) {
        return nonNull(productOperation.getProduct());
    }

    private ProductOperation parseEditOperation(ProductOperation productOperation) {
        List<ProductPatch> productPatches = new ArrayList<>();
        if (nonNull(productOperation.getProduct().getManufacturerName()))
            productPatches.add(new ProductPatch("manufacturerName", productOperation.getProduct().getManufacturerName()));
        if (nonNull(productOperation.getProduct().getModelName()))
            productPatches.add(new ProductPatch("modelName", productOperation.getProduct().getModelName()));
        if (nonNull(productOperation.getProduct().getPrice()))
            productPatches.add(new ProductPatch("price", productOperation.getProduct().getPrice()));
        productOperation.setProductPatches(productPatches);
        productOperation.setProduct(null);

        return productOperation;
    }
}
