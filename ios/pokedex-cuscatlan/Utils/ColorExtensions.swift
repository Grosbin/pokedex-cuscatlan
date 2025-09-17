//
//  ColorExtensions.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import SwiftUI

extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (1, 1, 1, 0)
        }

        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue:  Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}

// MARK: - Pokemon Type Colors
extension Color {
    static func pokemonTypeColor(for type: String) -> Color {
        switch type.lowercased() {
        case "fire", "fuego":
            return Color(hex: "FF6B6B")
        case "water", "agua":
            return Color(hex: "4ECDC4")
        case "grass", "planta":
            return Color(hex: "95E1A3")
        case "electric", "eléctrico":
            return Color(hex: "FFE66D")
        case "psychic", "psíquico":
            return Color(hex: "FF8A80")
        case "ice", "hielo":
            return Color(hex: "81D4FA")
        case "dragon", "dragón":
            return Color(hex: "9C27B0")
        case "dark", "siniestro":
            return Color(hex: "424242")
        case "fairy", "hada":
            return Color(hex: "E1BEE7")
        case "poison", "veneno":
            return Color(hex: "BA68C8")
        case "bug", "bicho":
            return Color(hex: "A5D6A7")
        case "ghost", "fantasma":
            return Color(hex: "9E9E9E")
        case "steel", "acero":
            return Color(hex: "B0BEC5")
        case "flying", "volador":
            return Color(hex: "90CAF9")
        case "normal":
            return Color(hex: "E0E0E0")
        case "fighting", "lucha":
            return Color(hex: "FF8A65")
        case "rock", "roca":
            return Color(hex: "BCAAA4")
        case "ground", "tierra":
            return Color(hex: "D7CCC8")
        default:
            return Color(hex: "E0E0E0")
        }
    }
}