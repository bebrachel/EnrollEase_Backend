package ru.nsu.enrollease.dto.request;

import java.util.Map;
import lombok.NonNull;

public record UpdateDataOrCreateOneRequest(@NonNull String iian, @NonNull Map<String, String> data) {

}
