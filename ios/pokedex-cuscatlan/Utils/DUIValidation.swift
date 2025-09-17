//
//  DUIValidation.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import Foundation

class DUIValidation {
    static func isValidDUI(_ dui: String) -> Bool {
        let digitsOnly = dui.filter { $0.isNumber }
        return !digitsOnly.isEmpty && digitsOnly.count >= 6 && digitsOnly.count <= 15
    }

    static func formatDUI(_ input: String) -> String {
        return input.filter { $0.isNumber }
    }
}

class CarnetValidation {
    static func isValidCarnet(_ carnet: String) -> Bool {
        let alphanumericPattern = "^[A-Za-z0-9]+$"
        let regex = try! NSRegularExpression(pattern: alphanumericPattern)
        let range = NSRange(location: 0, length: carnet.utf16.count)

        return regex.firstMatch(in: carnet, options: [], range: range) != nil &&
               carnet.count >= 3 && carnet.count <= 15
    }
}