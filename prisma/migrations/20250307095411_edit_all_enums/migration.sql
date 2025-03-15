/*
  Warnings:

  - You are about to alter the column `status` on the `bookings` table. The data in that column could be lost. The data in that column will be cast from `Enum(EnumId(3))` to `Enum(EnumId(1))`.
  - You are about to alter the column `payment_status` on the `payments` table. The data in that column could be lost. The data in that column will be cast from `Enum(EnumId(1))` to `Enum(EnumId(2))`.
  - The values [credit_card,bank_transfer,paypal,cash] on the enum `payments_payment_method` will be removed. If these variants are still used in the database, this will fail.

*/
-- AlterTable
ALTER TABLE `bookings` MODIFY `status` ENUM('PENDING', 'CONFIRMED', 'CANCELED', 'COMPLETED') NOT NULL DEFAULT 'PENDING';

-- AlterTable
ALTER TABLE `payments` MODIFY `payment_status` ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    MODIFY `payment_method` ENUM('CREDIT_CARD', 'BANK_TRANSFER', 'PAYPAL', 'CASH') NOT NULL;
