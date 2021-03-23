package studia.ium.warehouse.application.rest.product.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studia.ium.warehouse.application.rest.product.control.EditProductInputParser;
import studia.ium.warehouse.application.rest.product.control.SynchronizeInputParser;
import studia.ium.warehouse.application.rest.product.entity.ProductRestResource;
import studia.ium.warehouse.business.product.boundary.ProductService;
import studia.ium.warehouse.business.product.boundary.ProductSynchronizationService;
import studia.ium.warehouse.business.product.entity.ProductException;
import studia.ium.warehouse.business.product.entity.ProductOperation;
import studia.ium.warehouse.business.product.entity.ProductOperationSyncError;
import studia.ium.warehouse.business.product.entity.ProductPatch;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private ProductService productService;
    private ProductSynchronizationService productSynchronizationService;
    private EditProductInputParser editProductInputParser;
    private SynchronizeInputParser synchronizeInputParser;

    public ProductRestController(ProductService productService,
                                 ProductSynchronizationService productSynchronizationService,
                                 EditProductInputParser editProductInputParser,
                                 SynchronizeInputParser synchronizeInputParser) {
        this.productService = productService;
        this.productSynchronizationService = productSynchronizationService;
        this.editProductInputParser = editProductInputParser;
        this.synchronizeInputParser = synchronizeInputParser;
    }

    @GetMapping
    public ResponseEntity<?> findAllProducts() {
        return handleExceptionsFrom(this::handleFindAllProductsRequest);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRestResource product) {
        return handleExceptionsFrom(() -> handleAddProductRequest(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductById(@PathVariable UUID id) {
        return handleExceptionsFrom(() -> handleFindProductByIdRequest(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable UUID id, @RequestBody String jsonValue) {
        return handleExceptionsFrom(() -> handleEditProductRequest(id, jsonValue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        return handleExceptionsFrom(() -> handleDeleteProductRequest(id));
    }

    @PostMapping("/{id}/increaseQuantity")
    public ResponseEntity<?> increaseQuantityOfProduct(@PathVariable UUID id,
                                                       @RequestParam Integer amount,
                                                       @RequestParam(required = false, defaultValue = "0") Integer warehouseId) {
        return handleExceptionsFrom(() -> handleIncreaseQuantityOfProductRequest(id, amount, warehouseId));
    }

    @PostMapping("/{id}/decreaseQuantity")
    public ResponseEntity<?> decreaseQuantityOfProduct(@PathVariable UUID id,
                                                       @RequestParam Integer amount,
                                                       @RequestParam(required = false, defaultValue = "0") Integer warehouseId) {
        return handleExceptionsFrom(() -> handleDecreaseQuantityOfProductRequest(id, amount, warehouseId));
    }

    @PostMapping("/synchronize")
    public ResponseEntity<?> synchronize(@RequestBody List<ProductOperation> productOperations) {
        return handleExceptionsFrom(() -> handleSynchronizeRequest(productOperations));
    }

    private ResponseEntity<?> handleFindAllProductsRequest() {
        return getOkResponseWith(ProductRestResource.fromMultiple(productService.findAll()));
    }

    private ResponseEntity<?> handleFindProductByIdRequest(UUID id) {
        return getOkResponseWith(ProductRestResource.from(productService.findById(id)));
    }

    private ResponseEntity<?> handleAddProductRequest(ProductRestResource productRestResource) {
        productService.add(productRestResource.toProduct());

        return getCreatedResponse();
    }

    private ResponseEntity<?> handleEditProductRequest(UUID id, String jsonValue) {
        List<ProductPatch> productPatches = editProductInputParser.parse(jsonValue);
        productService.edit(id, productPatches, getCurrentTime());

        return getOkResponse();
    }

    private ResponseEntity<?> handleDeleteProductRequest(UUID id) {
        productService.delete(id);

        return getOkResponse();
    }

    private ResponseEntity<?> handleIncreaseQuantityOfProductRequest(UUID id, Integer amount, Integer warehouseId) {
        productService.increaseQuantityOfProduct(id, amount, warehouseId);

        return getOkResponse();
    }

    private ResponseEntity<?> handleDecreaseQuantityOfProductRequest(UUID id, Integer amount, Integer warehouseId) {
        productService.decreaseQuantityOfProduct(id, amount, warehouseId);

        return getOkResponse();
    }

    private ResponseEntity<?> handleSynchronizeRequest(List<ProductOperation> productOperations) {
        List<ProductOperation> parsedProductOperations = synchronizeInputParser.parse(productOperations);
        List<ProductOperationSyncError> errors = productSynchronizationService
            .synchronizeProductOperations(parsedProductOperations);

        return getOkResponseWith(errors);
    }

    private ResponseEntity<?> handleExceptionsFrom(Supplier<ResponseEntity<?>> requestHandler) {
        try {
            return requestHandler.get();
        } catch (ProductException e) {
            return getBadRequestResponseWith(e.getErrorMessages());
        }
    }

    private ResponseEntity<?> getCreatedResponse() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private ResponseEntity<?> getOkResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<?> getOkResponseWith(Object body) {
        return ResponseEntity
            .ok(body);
    }

    private ResponseEntity<?> getBadRequestResponseWith(Object body) {
        return ResponseEntity
            .badRequest()
            .body(body);
    }

    private long getCurrentTime() {
        return Instant.now().toEpochMilli();
    }
}
